package com.github.pvb

abstract class Processor[-In, +Out, -Session] {
  def name: String
  def process(input: In, session: Session): Out
}
