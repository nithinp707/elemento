@file:Suppress("MemberVisibilityCanBePrivate")

package com.zenora.elemento.common

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import com.zenora.elemento.BaseApplication
import com.zenora.elemento.common.constants.AppConstants
import org.apache.commons.io.FileUtils
import java.io.*
import java.net.URLConnection
import java.text.DecimalFormat
import java.util.*
import kotlin.math.min

/**
 * Utility for Fetching the File Paths for Attachments
 */
@Suppress("unused")
class FileUtil(private val context: Context) {

    companion object {
        private const val TAG = "FileUtil"

        /* File Types */
        private const val TEXT = "text/plain"
        private const val PDF = "application/pdf"
        private const val DOC = "application/msword"
        private const val DOCX =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        private const val IMAGE_JPG = "image/jpeg"
        private const val IMAGE_PNG = "image/png"
        private const val IMAGE_GIF = "image/gif"
        private const val VIDEO = "video/mp4"
        private const val PPT = "application/vnd.ms-powerpoint"
        private const val PPTX =
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        private const val XLS = "application/vnd.ms-excel"
        private const val XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        private const val DATA: String = "_data"
        private var contentUri: Uri? = null
    }


    /**
     * Function for checking whether file exists for the given file path
     */
    private fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }


    /**
     * Function for Fetching The File Path of Attachment
     */
    fun getFilePath(fileUri: Uri): String? {
        val selection: String
        val selectionArgs: Array<String>
        /*Document Provider/External Store*/
        if (isExternalStorageDocument(fileUri)) {
            val docId: String = DocumentsContract.getDocumentId(fileUri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val fullPath = getPathFromExtSD(split)
            return if (fullPath != "") fullPath else null
        }

        /*Downloads Provider*/
        if (isDownloadsDocument(fileUri)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val cursor = context.contentResolver.query(
                    fileUri,
                    arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )
                cursor.use {
                    if (it != null && it.moveToFirst()) {
                        val fileName = it.getString(0)
                        val path = BaseApplication.applicationContext()
                            .getExternalFilesDir(null)?.absolutePath
                            .toString() + "/Download/" + fileName
                        if (!TextUtils.isEmpty(path)) {
                            return path
                        }
                    }
                }
                val id: String = DocumentsContract.getDocumentId(fileUri)
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) return id.replaceFirst("raw:".toRegex(), "")
                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                    )
                    for (contentUriPrefix in contentUriPrefixesToTry) try {
                        val contentUri: Uri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix), id.toLong()
                        )
                        return getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        //In Android 8 and Android P the id is not a number
                        if (fileUri.path != null)
                            return fileUri.path!!.replaceFirst(
                                "^/document/raw:".toRegex(), ""
                            ).replaceFirst("^raw:".toRegex(), "")
                    }
                }
            } else {
                val id: String = DocumentsContract.getDocumentId(fileUri)
                if (id.startsWith("raw:"))
                    return id.replaceFirst("raw:".toRegex(), "")
                try {
                    contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), id.toLong()
                    )
                } catch (e: NumberFormatException) {
                    Log.e(TAG, e.message!!)
                }
                if (contentUri != null)
                    return getDataColumn(context, contentUri, null, null)
            }
        }
        /*Media Provider*/
        if (isMediaDocument(fileUri)) {
            val docId: String = DocumentsContract.getDocumentId(fileUri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            selection = "_id=?"
            selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }

        /*Google Drive Provider*/
        if (isGoogleDriveUri(fileUri)) return getDriveFilePath(fileUri)

        /*WatsApp Provider*/
        if (isWhatsAppFile(fileUri)) return getFilePathForWhatsApp(fileUri)

        /*Media Store & General Provider*/
        if ("content".equals(fileUri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(fileUri)) return fileUri.lastPathSegment
            if (isGoogleDriveUri(fileUri)) return getDriveFilePath(fileUri)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                copyFileToInternalStorage(fileUri, "userfiles")
            else
                getDataColumn(context, fileUri, null, null)
        }

        /*File Provider*/
        return if ("file".equals(fileUri.scheme, ignoreCase = true)) fileUri.path else null
    }


    /**
     * Function for fetching path from SD card
     */
    private fun getPathFromExtSD(pathData: Array<String>): String {
        val type = pathData[0]
        val relativePath = "/" + pathData[1]
        var fullPath: String
        if ("primary".equals(type, ignoreCase = true)) {
            fullPath = BaseApplication.applicationContext()
                .getExternalFilesDir(null)?.absolutePath + relativePath
            if (isFileExists(fullPath)) return fullPath
        }
        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv(AppConstants.SECONDARY_STORAGE) ?: "" + relativePath
        if (isFileExists(fullPath)) return fullPath
        fullPath = System.getenv(AppConstants.EXTERNAL_STORAGE) ?: "" + relativePath
        return if (isFileExists(fullPath)) fullPath else fullPath
    }


    /**
     * Function for Fetching Google Drive Path
     */
    fun getDriveFilePath(uri: Uri?): String {
        val returnCursor = context.contentResolver.query(
            uri!!, null,
            null, null, null
        )
        /*
         * Get the column indexes of the data in the Cursor,
         * Move to the first row in the Cursor, get the data, and display it.
         */
        val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: 0

        if (returnCursor == null) return ""
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)

        val file = File(context.cacheDir, name)
        try {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    var read: Int
                    val maxBufferSize = 1024 * 1024
                    val bytesAvailable: Int
                    if (inputStream != null) {
                        bytesAvailable = inputStream.available()
                        val bufferSize = min(bytesAvailable, maxBufferSize)
                        val buffers = ByteArray(bufferSize)
                        while (inputStream.read(buffers)
                                .also { read = it } != -1
                        ) outputStream.write(buffers, 0, read)
                        inputStream.close()
                        outputStream.close()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        } finally {
            returnCursor.close()
        }
        return file.path
    }


    /***
     * Used for Android Q+
     * @param uri
     * @param newDirName if you want to create a directory, you can set this variable
     * @return
     */
    private fun copyFileToInternalStorage(uri: Uri, newDirName: String): String {
        val returnCursor = context.contentResolver.query(
            uri, arrayOf(
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
            ), null, null, null
        )

        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,and display it.
         */
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val output: File = if (newDirName != "") {
            val dir = File(context.filesDir.toString() + "/" + newDirName)
            if (!dir.exists()) dir.mkdir()
            File(context.filesDir.toString() + "/" + newDirName + "/" + name)
        } else File(context.filesDir.toString() + "/" + name)
        try {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                FileOutputStream(output).use { outputStream ->
                    var read: Int
                    val bufferSize = 1024
                    val buffers = ByteArray(bufferSize)
                    while (inputStream!!.read(buffers).also { read = it } != -1)
                        outputStream.write(buffers, 0, read)
                    outputStream.close()
                    inputStream.close()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        } finally {
            returnCursor.close()
        }
        return output.path
    }


    private fun getFilePathForWhatsApp(uri: Uri): String {
        return copyFileToInternalStorage(uri, AppConstants.DIRECTORY_WHATSAPP)
    }


    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return uri.authority == AppConstants.EXTERNAL_STORAGE_DOCUMENT
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return uri.authority == AppConstants.DOWNLOAD_DOCUMENT
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return uri.authority == AppConstants.MEDIA_DOCUMENT
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return uri.authority == AppConstants.GOOGLE_PHOTO_URI
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Whats App Media.
     */
    private fun isWhatsAppFile(uri: Uri): Boolean {
        return uri.authority == AppConstants.WHATSAPP_MEDIA
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Drive Media.
     */
    private fun isGoogleDriveUri(uri: Uri): Boolean {
        return uri.authority == AppConstants.GOOGLE_DRIVE_URI
                || uri.authority == AppConstants.GOOGLE_DRIVE_URI_LEGACY
    }


    /**
     * Function for fetching App folder file path
     *
     * @return : file path for app
     */
    val appFilePath: File?
        get() = BaseApplication.applicationContext().getExternalFilesDir(
            Environment.DIRECTORY_DOWNLOADS
                    + File.separator + AppConstants.ROOT_DIRECTORY_NAME + File.separator
        )


    /**
     * Function for generating files with specific extension inside App files folder
     *
     * @param fileName   : name of the file
     * @param mExtension :file type
     * @return : file
     */
    fun generateNewFile(fileName: Long, mExtension: String): File {
        val dir = File(appFilePath.toString())
        if (!dir.exists()) dir.mkdirs()
        return File(dir.toString(), fileName.toString() + mExtension)
    }


    /**
     * Function for generating file inside App file folder
     *
     * @param fileName :  name of the file
     * @return : file
     */
    fun generateFile(fileName: String): File {
        val dir = File(appFilePath.toString())
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return File(dir.toString(), fileName)
    }


    /**
     * Function for generating a temporary folder for saving & deleting temp content
     *
     * @param fileName   : name of the file
     * @param mExtension :  file type
     * @return :  file
     */
    fun generateTempDir(fileName: Long, mExtension: String): File {
        val dir = File(appFilePath.toString() + File.separator + AppConstants.TEMP_DIRECTORY)
        if (!dir.exists()) dir.mkdirs()
        return File(dir.toString(), fileName.toString() + mExtension)
    }


    /**
     * Function for deleting temporary directory
     */
    fun deleteTempDir() {
        val mTempDir =
            File(appFilePath.toString() + File.separator + AppConstants.TEMP_DIRECTORY)
        try {
            FileUtils.deleteDirectory(mTempDir)
        } catch (e: IOException) {
            Log.e(TAG, e.message!!)
        }
    }


    /**
     * Check if the file is image
     *
     * @param path : file path
     * @return : whether file is of image type
     */
    fun isImageFile(path: String?): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }


    /**
     * Check if the file is video
     *
     * @param path : file path
     * @return : whether the file is of video type
     */
    private fun isVideoFile(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
    }


    /**
     * Function for checking if file is a document file
     *
     * @param fileName : file name
     * @return : whether file is a document or not
     */
    fun isDocumentFile(fileName: String): Boolean {
        fileName.replace(" ", "")
        val extension: String? = MimeTypeMap.getFileExtensionFromUrl(fileName)
        if (!extension.isNullOrEmpty()) {
            val type: String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (type != null) return type == TEXT || type == PDF || type == DOC || type == DOCX
                    || type == PPT || type == PPTX || type == XLS || type == XLSX
        }
        return false
    }


    /**
     * Fetch the list of all the documents in the phone
     *
     * @param parentDir : file directory
     * @return : list of files
     */
    fun getDocumentsList(parentDir: File): List<File> {
        val filesList = ArrayList<File>()
        val files = parentDir.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files)
                if (file.isDirectory) filesList.addAll(getDocumentsList(file))
                else if (isDocumentFile(file.name)
                ) filesList.add(file)
        }
        filesList.sortWith(fun(o1: File, o2: File): Int {
            return o2.lastModified().compareTo(o1.lastModified())
        })
        return filesList
    }


    /**
     * Fetch mime type of a file
     *
     * @param url : file url
     * @return : mime type of url
     */
    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension: String? = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        return type
    }


    /**
     * Fetch the file extension from URI
     *
     * @param uri : file url
     * @return : File extension
     */
    fun getFileExtension(uri: Uri): String {
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString())
    }


    /**
     * Write the given bitmap into the given file. JPEG is used as the compression format with
     * quality set
     * to 100.
     *
     * @param bm   The bitmap.
     * @param file The file to write the bitmap into.
     */
    @Throws(IOException::class)
    fun writeBitmapToFile(bm: Bitmap, file: File?, quality: Int) {
        FileOutputStream(file).use { fileOutputStream ->
            bm.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
            fileOutputStream.flush()
        }
    }


    /**
     * Function for checking if file type is permitted
     *
     * @param fileName   : name of the file
     * @return : whether file permitted or not
     */
    fun isPermittedFile(fileName: String): Boolean {
        fileName.replace(" ", "")
        val extension = getFileExtensionFromUrl(fileName)
        if (!extension.isNullOrEmpty()) {
            val type: String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (type != null) {
                return type == DOC || type == DOCX || type == TEXT || type == PDF
                        || type == XLS || type == XLSX || type == IMAGE_JPG
                        || type == IMAGE_PNG || type == IMAGE_GIF || type == VIDEO
            }
        }
        return false
    }


    /**
     * Function for getting the file extension from filename
     *
     * @param fileName - full name of the file with extension
     * @return - file extension
     */
    fun getFileExtensionFromUrl(fileName: String): String? {
        var fileExtension: String? = null
        val i = fileName.lastIndexOf('.')
        if (i > 0) {
            fileExtension = fileName.substring(i + 1)
        }
        return fileExtension
    }


    /**
     * Function for checking if the attachment file size is permitted
     *
     * @param file : file
     * @return : permitted file size
     */
    fun permittedFileSize(file: File, permittedFileSize: Int): Boolean {
        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        val fileSizeInKB = fileSizeInBytes / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        val fileSizeInMB = fileSizeInKB / 1024
        return fileSizeInMB <= permittedFileSize
    }


    /**
     * Function for returning all the folder paths based on the type {Image/Videos}
     *
     * @param context : context
     * @return : list of strings
     */
    fun getDirectoryPaths(context: Context, isMediaTypeImage: Boolean): ArrayList<String> {
        val mediaFolders = ArrayList<String>()
        val mediaItemHashSet = HashSet<String>()
        val uri: Uri = if (isMediaTypeImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DISPLAY_NAME, DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            try {
                cursor.moveToFirst()
                do {
                    mediaItemHashSet.add(cursor.getString(cursor.getColumnIndexOrThrow(DATA)))
                } while (cursor.moveToNext())
            } catch (e: Exception) {
                Log.e(TAG, e.message!!)
            } finally {
                cursor.close()
            }
        }

        /*Return The Respective Media Folders*/
        for (mediaName in mediaItemHashSet) {
            val file = File(mediaName)
            if (!file.parent.isNullOrEmpty()) {
                val folder =
                    file.parent?.substring(file.parent?.lastIndexOf(File.separator) ?: 0 + 1)
                var folderPaths = mediaName.substring(0, mediaName.lastIndexOf("$folder/"))
                folderPaths = "$folderPaths$folder/"
                if (!mediaFolders.contains(folderPaths)) mediaFolders.add(folderPaths)
            }
        }
        return mediaFolders
    }


    /**
     * Search a directory and return a list of all **files** contained inside
     *
     * @param directory        : file directory
     * @param isMediaTypeImage : media type image or not
     * @return : list of string
     */
    fun getFilePaths(directory: String, isMediaTypeImage: Boolean): ArrayList<String> {
        val pathArray = ArrayList<String>()
        val file = File(directory)
        val filesList = file.listFiles()
        if (filesList != null && filesList.isNotEmpty()) {
            Arrays.sort(filesList) { f1: File, f2: File ->
                f2.lastModified().compareTo(f1.lastModified())
            }
            for (value in filesList) if (value.isFile && value.length() > 0) {
                if (isMediaTypeImage) {
                    if (isImageFile(value.absolutePath)) pathArray.add(value.absolutePath)
                } else if (isVideoFile(value.absolutePath)) pathArray.add(value.absolutePath)
            }
        }
        return pathArray
    }


    /**
     * Function for rotating landscape images to portrait in Samsung Devices.
     *
     * @param file : file
     * @return : rotated image
     */
    fun rotateImageFile(file: File?): File? {
        if (file == null) return null
        try {
            val exif = ExifInterface(file.path)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            var angle = 0
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> angle = AppConstants.APP_ORIENTATION_90
                ExifInterface.ORIENTATION_ROTATE_180 -> angle = AppConstants.APP_ORIENTATION_180
                ExifInterface.ORIENTATION_ROTATE_270 -> angle = AppConstants.APP_ORIENTATION_270
            }
            if (angle == 0) return file
            val mat = Matrix()
            mat.postRotate(angle.toFloat())
            val bmp: Bitmap? = BitmapFactory.decodeStream(
                FileInputStream(file),
                null, null
            )
            val correctBmp: Bitmap
            if (bmp != null) {
                correctBmp = Bitmap.createBitmap(
                    bmp, 0, 0, bmp.width,
                    bmp.height, mat, true
                )
                // transfer bytes from the input file to the output file
                try {
                    FileOutputStream(file).use { myOutput ->
                        ByteArrayOutputStream().use { byteArrayOutputStream ->
                            correctBmp.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                byteArrayOutputStream
                            )
                            val bitmapData = byteArrayOutputStream.toByteArray()
                            // Open the empty db as the output stream
                            myOutput.write(bitmapData)
                            // Close the streams
                            myOutput.flush()
                            myOutput.close()
                            return file
                        }
                    }
                } catch (e: IOException) {
                    if (e.message != null) Log.e(TAG, e.message!!)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error in setting image")
        } catch (oom: OutOfMemoryError) {
            Log.e(TAG, "OOM Error in setting image")
        }
        return null
    }


    /**
     * Function for getting the file name from path
     *
     * @param path :  path of the file
     * @return : file name
     */
    fun getFileName(path: String?): String {
        return if (!path.isNullOrEmpty()) {
            path.substring(path.lastIndexOf("/") + 1)
        } else {
            ""
        }
    }


    /**
     * Function for getting the file type
     *
     * @param fileName :  name of the file
     * @return : file type
     */
    fun getFileType(fileName: String): String {
        fileName.replace(" ", "")
        val extension: String? = MimeTypeMap.getFileExtensionFromUrl(fileName)
        if (!extension.isNullOrEmpty()) {
            val type: String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (type != null) {
                when (type) {
                    DOC -> return DOC
                    DOCX -> return DOCX
                    TEXT -> return TEXT
                    PDF -> return PDF
                    IMAGE_JPG -> return IMAGE_JPG
                    IMAGE_PNG -> return IMAGE_PNG
                    IMAGE_GIF -> return IMAGE_GIF
                    VIDEO -> return VIDEO
                    else -> Log.d(TAG, "type :$type")
                }
            }
        }
        return DOC
    }

    /**
     * Function for getting the file size in KiloBytes
     *
     * @param file :  original file
     * @return : file size in KB
     */
    fun getFileToKiloBytes(file: File): String {
        return getStringSizeLengthFile(file.length())
    }


    /**
     * Function for getting the file size in KiloBytes
     *
     * @param length :  original file size in bytes
     * @return : file size in KB
     */
    fun getFileSizeKiloBytes(length: Long): String {
        return getStringSizeLengthFile(length)
    }


    /**
     * Function for calculating the file size
     *
     * @param size :  original file size in bytes
     * @return : file size
     */
    fun getStringSizeLengthFile(size: Long): String {
        val decimalFormat = DecimalFormat("0.00")
        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTerra = sizeGb * sizeKb
        return when {
            size < sizeMb -> decimalFormat.format((size / sizeKb).toDouble()) + " KB"
            size < sizeGb -> decimalFormat.format((size / sizeMb).toDouble()) + " MB"
            size < sizeTerra -> decimalFormat.format((size / sizeGb).toDouble()) + " GB"
            else -> ""
        }
    }

}