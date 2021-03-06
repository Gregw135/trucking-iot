package com.orendainx.hortonworks.trucking.simulator.flows

import akka.actor.{ActorRef, PoisonPill, Props, Terminated}
import com.orendainx.hortonworks.trucking.common.models.{TrafficData, TruckData}
import com.orendainx.hortonworks.trucking.simulator.flows.FlowManager.ShutdownFlow
import com.orendainx.hortonworks.trucking.simulator.transmitters.DataTransmitter.Transmit

/**
  * The TruckAndTrafficFlowManager expects messages of type [[Transmit(data: TruckData]] and [[Transmit(data: TrafficData]]
  * and routes messages to two sepearate [[com.orendainx.hortonworks.trucking.simulator.transmitters.DataTransmitter]]s
  * specified as arguments to [[TruckAndTrafficFlowManager.props()]].
  *
  * @author Edgar Orendain <edgar@orendainx.com>
  */
object TruckAndTrafficFlowManager {

  def props(truckTransmitter: ActorRef, trafficTransmitter: ActorRef) =
    Props(new TruckAndTrafficFlowManager(truckTransmitter, trafficTransmitter))
}

class TruckAndTrafficFlowManager(truckTransmitter: ActorRef, trafficTransmitter: ActorRef) extends FlowManager {

  var transmittersTerminated = 0

  def receive = {
    case Transmit(data: TruckData) => truckTransmitter ! Transmit(data)
    case Transmit(data: TrafficData) => trafficTransmitter ! Transmit(data)

    case ShutdownFlow =>
      truckTransmitter ! PoisonPill
      trafficTransmitter ! PoisonPill
      context watch truckTransmitter
      context watch trafficTransmitter

    case Terminated(_) =>
      transmittersTerminated += 1
      if (transmittersTerminated == 2) context stop self
  }
}
