package dev.ybrmst.dicodingevents.domain.models

sealed class AppError : Throwable() {
  data class Network(override val message: String = "We are having trouble connecting to the server.\nPlease check your internet connection.") :
    AppError()

  data class BadRequest(override val message: String = "Uh oh! Something went wrong.\nPlease try again.") :
    AppError()

  data class NotFound(override val message: String = "The requested resource could not be found :(") :
    AppError()

  data class InternalServerError(override val message: String = "Looks like there's a problem.\nPlease try again later...") :
    AppError()
}