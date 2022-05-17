package models

import controllers.Action

case class EventWithAction(event: Event, action: Action)