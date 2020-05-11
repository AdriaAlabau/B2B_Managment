package TFG_project.SCALA
import collection.mutable._
import collection.mutable._
import collection.JavaConverters._
import collection.JavaConverters._
import scala.Array.ofDim

object Encoding {
  def codificar(nMeetings : Int, nSlots: Int, nSessions : Int, nAttendeesParticipant : Array[Integer], taulesXSessio : Array[Array[Integer]], mettingXParticipant : Array[Array[Integer]],  forbidden: Array[Array[Integer]], gapSlots: Array[Array[Integer]] , gapMeetings: Array[Array[Integer]], participants : Array[Array[Integer]] ){

    var e = new ScalAT("/Users/jordic/Desktop/","/Users/jordic/Desktop/");

    //Variables del viewpoint
    var schedule = e.newVar2DArray(nMeetings,nSlots)

    //CONSTRAINT 1
    for(p <- mettingXParticipant.indices; ts <- 0 until nSlots) {
      e.addAMK((for (i <- mettingXParticipant(p)) yield (schedule(i)(ts))).toList, nAttendeesParticipant(p))
    }


    //CONSTRAINT 2
    for(p <- participants.indices; forb <- forbidden(p); meet <- mettingXParticipant(p))
    {
        e.addClause((-(schedule(meet)(forb))) :: List())
    }

    //CONSTRAINT 3 REVISAR ELS CASOS ALL
    //CONSTRAINT 4
    for(tG <- 0 until nSessions; i <- gapMeetings(tG))
    {
      e.addEOQuad((for (j <- gapSlots(tG)) yield (schedule(i)(j))).toList)
      for(g2 <- 0 until nSessions) {
        if (g2 != tG) {
          for (slot <- gapSlots(g2)) {
            e.addClause(-schedule(i)(slot) :: List())
          }
        }
      }
    }

    //PER ACABAR
    //CONSTRAINT 5
    for(tSes <- 0 until nSessions; tSlot <- gapSlots(tSes); i <- gapMeetings(tSlot))
      {
        e.addEOQuad((for (j <- gapSlots(tSlot)) yield (schedule(i)(j))).toList)
      }


    //CONSTRAINT 6
    for(tSes <- 0 until nSessions; tSlot <- gapSlots(tSes))
    {
      e.addAMK((for (i <- gapMeetings(tSes)) yield schedule(i)(tSlot)).toList, taulesXSessio(tSes))
    }

    //Solucionem
    if (e.solve()) {

    //  for(i <- 0 to 8){
       // for(j<- 0 to 8) {
       //   for (k <- 0 to 8)
       ///     if (e.getValue(graella(i)(j)(k))) print(k+1+" ")
       //   if(j%3==2) print(" ")
       // }
      //  println
      //  if(i%3==2) println
     // }

    }

    //Printem estadistiques de solucio
    println("Solving time: " + e.getTime.toString + " seconds")
    println("N vars: " + e.getNVars)
    println("N clauses: " +  e.getNClauses)
    println("N decisions: " + e.getDecisions)
    println("N propagations: " + e.getPropagations)
    println("N conflicts: " + e.getConflicts)

  }

}