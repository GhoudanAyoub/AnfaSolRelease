package ghoudan.anfaSolution.com.networking.exception

import java.io.IOException

class HttpUnauthorizedException : IOException()

class HttpBadRequestException : IOException()

class HttpInternalErrorException : IOException()

class HttpNoContentException : IOException()

class HttpNotFoundException : IOException()

class HttpConflictException : IOException()

class HttpNoInternetException : IOException()

class HttpForbiddenException : IOException()

class HttpNotAcceptableException : IOException()

class HttpLockedException : IOException()
