package pvb.runtime

case class AssertionError(message: String) extends Error(message)