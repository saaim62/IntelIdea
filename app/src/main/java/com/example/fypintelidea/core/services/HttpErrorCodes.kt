package com.example.fypintelidea.core.services

enum class HttpErrorCodes(val code: Int) {

    OK(200),
    Created(201),
    Accepted(202),

    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404),
    RequestTimeout(408),
    UnsupportedMediaType(415),

    InternalServerError(500),
    BadGateway(502),
    ServiceUnavailable(503),

    Unknown(0)
}