package com.zenora.elemento.common.network.models

import com.zenora.elemento.common.network.NoNetworkException

/**
 * This class handles all the different types of API Response Codes.
 */

class HttpStatus
/**
 * Returns HTTP Status Code & Error Message
 *
 * @param code         : response code
 * @param reasonPhrase : response phrase
 */ private constructor(val code: Int, private val reasonPhrase: String) {
    companion object {
        /*No Network Status Code*/
        //const val NO_NETWORK = 1200

        /*Retrofit Failure Status Code*/
        //const val RETROFIT_FAILURE = 1205

        /**
         * 100 Continue
         */
        private val CONTINUE = HttpStatus(100, "Continue")

        /**
         * 101 Switching Protocols
         */
        private val SWITCHING_PROTOCOLS = HttpStatus(101, "Switching Protocols")

        /**
         * 102 Processing (WebDAV, RFC2518)
         */
        private val PROCESSING = HttpStatus(102, "Processing")

        /**
         * 200 OK
         */
        private val OK = HttpStatus(200, "OK")

        /**
         * 201 Created
         */
        private val CREATED = HttpStatus(201, "Created")

        /**
         * 202 Accepted
         */
        private val ACCEPTED = HttpStatus(202, "Accepted")

        /**
         * 203 Non-Authoritative Information (since HTTP/1.1)
         */
        private val NON_AUTHORITATIVE_INFORMATION = HttpStatus(
            203,
            "Non-Authoritative Information"
        )

        /**
         * 204 No Content
         */
        private val NO_CONTENT = HttpStatus(204, "No Content")

        /**
         * 205 Reset Content
         */
        private val RESET_CONTENT = HttpStatus(205, "Reset Content")

        /**
         * 206 Partial Content
         */
        private val PARTIAL_CONTENT = HttpStatus(206, "Partial Content")

        /**
         * 207 Multi-Status (WebDAV, RFC2518)
         */
        private val MULTI_STATUS = HttpStatus(207, "Multi-Status")

        /**
         * 300 Multiple Choices
         */
        private val MULTIPLE_CHOICES = HttpStatus(300, "Multiple Choices")

        /**
         * 301 Moved Permanently
         */
        private val MOVED_PERMANENTLY = HttpStatus(301, "Moved Permanently")

        /**
         * 302 Found
         */
        private val FOUND = HttpStatus(302, "Found")

        /**
         * 303 See Other (since HTTP/1.1)
         */
        private val SEE_OTHER = HttpStatus(303, "See Other")

        /**
         * 304 Not Modified
         */
        private val NOT_MODIFIED = HttpStatus(304, "Not Modified")

        /**
         * 305 Use Proxy (since HTTP/1.1)
         */
        private val USE_PROXY = HttpStatus(305, "Use Proxy")

        /**
         * 307 Temporary Redirect (since HTTP/1.1)
         */
        private val TEMPORARY_REDIRECT = HttpStatus(307, "Temporary Redirect")

        /**
         * 400 Bad Request
         */
        private val BAD_REQUEST = HttpStatus(400, "Bad Request")

        /**
         * 401 Unauthorized
         */
        private val UNAUTHORIZED = HttpStatus(401, "Unauthorized")

        /**
         * 402 Payment Required
         */
        private val PAYMENT_REQUIRED = HttpStatus(402, "Payment Required")

        /**
         * 403 Forbidden
         */
        private val FORBIDDEN = HttpStatus(403, "Forbidden")

        /**
         * 404 Not Found
         */
        private val NOT_FOUND = HttpStatus(404, "Not Found")

        /**
         * 405 Method Not Allowed
         */
        private val METHOD_NOT_ALLOWED = HttpStatus(
            405,
            "Method Not Allowed"
        )

        /**
         * 406 Not Acceptable
         */
        private val NOT_ACCEPTABLE = HttpStatus(406, "Not Acceptable")

        /**
         * 407 Proxy Authentication Required
         */
        private val PROXY_AUTHENTICATION_REQUIRED = HttpStatus(
            407,
            "Proxy Authentication Required"
        )

        /**
         * 408 Request Timeout
         */
        private val REQUEST_TIMEOUT = HttpStatus(408, "Request Timeout")

        /**
         * 409 Conflict
         */
        private val CONFLICT = HttpStatus(409, "Conflict")

        /**
         * 410 Gone
         */
        private val GONE = HttpStatus(410, "Gone")

        /**
         * 411 Length Required
         */
        private val LENGTH_REQUIRED = HttpStatus(411, "Length Required")

        /**
         * 412 Precondition Failed
         */
        private val PRECONDITION_FAILED = HttpStatus(
            412,
            "Precondition Failed"
        )

        /**
         * 413 Request Entity Too Large
         */
        private val REQUEST_ENTITY_TOO_LARGE = HttpStatus(
            413,
            "Request Entity Too Large"
        )

        /**
         * 414 Request-URI Too Long
         */
        private val REQUEST_URI_TOO_LONG = HttpStatus(
            414,
            "Request-URI Too Long"
        )

        /**
         * 415 Unsupported Media Type
         */
        private val UNSUPPORTED_MEDIA_TYPE = HttpStatus(
            415,
            "Unsupported Media Type"
        )

        /**
         * 416 Requested Range Not Satisfiable
         */
        private val REQUESTED_RANGE_NOT_SATISFIABLE = HttpStatus(
            416,
            "Requested Range Not Satisfiable"
        )

        /**
         * 417 Expectation Failed
         */
        private val EXPECTATION_FAILED = HttpStatus(
            417,
            "Expectation Failed"
        )

        /**
         * 422 Un-processable Entity (WebDAV, RFC4918)
         */
        private val UN_PROCESSABLE_ENTITY = HttpStatus(
            422,
            "Un-processable Entity"
        )

        /**
         * 423 Locked (WebDAV, RFC4918)
         */
        private val LOCKED = HttpStatus(423, "Locked")

        /**
         * 424 Failed Dependency (WebDAV, RFC4918)
         */
        private val FAILED_DEPENDENCY = HttpStatus(424, "Failed Dependency")

        /**
         * 425 Unordered Collection (WebDAV, RFC3648)
         */
        private val UNORDERED_COLLECTION = HttpStatus(425, "Unordered Collection")

        /**
         * 426 Upgrade Required (RFC2817)
         */
        private val UPGRADE_REQUIRED = HttpStatus(426, "Upgrade Required")

        /**
         * 500 Internal Server Error
         */
        private val INTERNAL_SERVER_ERROR = HttpStatus(500, "Internal Server Error")

        /**
         * 501 Not Implemented
         */
        private val NOT_IMPLEMENTED = HttpStatus(501, "Not Implemented")

        /**
         * 502 Bad Gateway
         */
        private val BAD_GATEWAY = HttpStatus(502, "Bad Gateway")

        /**
         * 503 Service Unavailable
         */
        private val SERVICE_UNAVAILABLE = HttpStatus(503, "Service Unavailable")

        /**
         * 504 Gateway Timeout
         */
        private val GATEWAY_TIMEOUT = HttpStatus(504, "Gateway Timeout")

        /**
         * 505 HTTP Version Not Supported
         */
        private val HTTP_VERSION_NOT_SUPPORTED = HttpStatus(
            505,
            "HTTP Version Not Supported"
        )

        /**
         * 506 Variant Also Negotiates (RFC2295)
         */
        private val VARIANT_ALSO_NEGOTIATES = HttpStatus(
            506,
            "Variant Also Negotiates"
        )

        /**
         * 507 Insufficient Storage (WebDAV, RFC4918)
         */
        private val INSUFFICIENT_STORAGE = HttpStatus(
            507,
            "Insufficient Storage"
        )

        /**
         * 510 Not Extended (RFC2774)
         */
        private val NOT_EXTENDED = HttpStatus(510, "Not Extended")

        /**
         * 1200 No Network Connection
         */
        private val NO_NETWORK_CONNECTION = HttpStatus(1200, NoNetworkException().message)

        /**
         * 1205 Something Went Wrong
         */
        private val SOMETHING_WENT_WRONG = HttpStatus(1205, "Something Went Wrong")
        fun valueOf(code: Int): HttpStatus {
            return when (code) {
                100 -> CONTINUE
                101 -> SWITCHING_PROTOCOLS
                102 -> PROCESSING
                200 -> OK
                201 -> CREATED
                202 -> ACCEPTED
                203 -> NON_AUTHORITATIVE_INFORMATION
                204 -> NO_CONTENT
                205 -> RESET_CONTENT
                206 -> PARTIAL_CONTENT
                207 -> MULTI_STATUS
                300 -> MULTIPLE_CHOICES
                301 -> MOVED_PERMANENTLY
                302 -> FOUND
                303 -> SEE_OTHER
                304 -> NOT_MODIFIED
                305 -> USE_PROXY
                307 -> TEMPORARY_REDIRECT
                400 -> BAD_REQUEST
                401 -> UNAUTHORIZED
                402 -> PAYMENT_REQUIRED
                403 -> FORBIDDEN
                404 -> NOT_FOUND
                405 -> METHOD_NOT_ALLOWED
                406 -> NOT_ACCEPTABLE
                407 -> PROXY_AUTHENTICATION_REQUIRED
                408 -> REQUEST_TIMEOUT
                409 -> CONFLICT
                410 -> GONE
                411 -> LENGTH_REQUIRED
                412 -> PRECONDITION_FAILED
                413 -> REQUEST_ENTITY_TOO_LARGE
                414 -> REQUEST_URI_TOO_LONG
                415 -> UNSUPPORTED_MEDIA_TYPE
                416 -> REQUESTED_RANGE_NOT_SATISFIABLE
                417 -> EXPECTATION_FAILED
                422 -> UN_PROCESSABLE_ENTITY
                423 -> LOCKED
                424 -> FAILED_DEPENDENCY
                425 -> UNORDERED_COLLECTION
                426 -> UPGRADE_REQUIRED
                500 -> INTERNAL_SERVER_ERROR
                501 -> NOT_IMPLEMENTED
                502 -> BAD_GATEWAY
                503 -> SERVICE_UNAVAILABLE
                504 -> GATEWAY_TIMEOUT
                505 -> HTTP_VERSION_NOT_SUPPORTED
                506 -> VARIANT_ALSO_NEGOTIATES
                507 -> INSUFFICIENT_STORAGE
                510 -> NOT_EXTENDED
                1200 -> NO_NETWORK_CONNECTION
                1205 -> SOMETHING_WENT_WRONG
                else -> getOtherStatus(code)
            }
        }

        private fun getOtherStatus(code: Int): HttpStatus {
            val reasonPhrase: String = when {
                code < 100 -> {
                    "Unknown Status"
                }
                code < 200 -> {
                    "Informational"
                }
                code < 300 -> {
                    "Successful"
                }
                code < 400 -> {
                    "Redirection"
                }
                code < 500 -> {
                    "Client Error"
                }
                code < 600 -> {
                    "Server Error"
                }
                else -> {
                    "Unknown Status"
                }
            }
            return HttpStatus(code, "$reasonPhrase ($code)")
        }
    }
}