package TFG_project.SCALA
import java.util

import TFG_project.HELPERS.SimpleClass

import collection.mutable._
import collection.mutable._
import scala.Array.ofDim
import scala.jdk.CollectionConverters._

object Encoding {

  def convertirJavaAScala(llistat : java.util.ArrayList[java.util.ArrayList[Int]]) : Array[Array[Int]] =
  {
    var auxiliar = llistat.asScala.toArray
    val llist : Array[Array[Int]] = new Array[Array[Int]](auxiliar.length)
    for(aux <- auxiliar.indices)
      llist(aux) = auxiliar(aux).asScala.toArray

    llist
  }

  def convertirScalaAJava(llistat : Array[Array[Int]]) : java.util.ArrayList[java.util.ArrayList[Int]] =
  {
    var lRet = new java.util.ArrayList[java.util.ArrayList[Int]]()
    for(i <- llistat.indices)
    {
      var aux = new util.ArrayList[Int]()
      aux.addAll(llistat(i).toList.asJava)
      lRet.add(aux)
    }

    lRet
  }

  /**
   *
   * @param nMeetings Contador de Reunions
   * @param nSlots Contador Total Slots
   * @param nSessions Contador Total de Reunions
   * @param nAP attendesXParticipan contador de persones d'una entitat
   * @param tXS taulesXSessio Per cada sessio el nombre de taules tipus N que disposa
   * @param mXP entityMeetings Per cada entitat, les seves reunions
   * @param fB forbidden per cada entitat, els slots als quals no assisteix
   * @param sS sessioSlots per cada sessio, els slots que en formen part
   * @param sM sessioMeetings per cada sessio, les reunions que poden tenir lloc
   * @param mS  meetingSessions per cada reunio, les sessions a les que es pot produir
   * @param mP meetingEntities per cada reunio, els seus participans
   * @return
   */

  def codificar(nMeetings : Int, nSlots: Int, nSessions : Int,
                nAP : java.util.ArrayList[Int],
                tXS : java.util.ArrayList[Int], // Capacitat, ntaules
                mXP : java.util.ArrayList[java.util.ArrayList[Int]],
                fB: java.util.ArrayList[java.util.ArrayList[Int]],
                sS: java.util.ArrayList[java.util.ArrayList[Int]],
                sM: java.util.ArrayList[java.util.ArrayList[Int]],
                mS: java.util.ArrayList[java.util.ArrayList[Int]],
                mP: java.util.ArrayList[java.util.ArrayList[Int]],
                pM: java.util.ArrayList[SimpleClass]) : java.util.ArrayList[java.util.ArrayList[java.util.ArrayList[Int]]] = {


    val attendesXParticipan = nAP.asScala.toArray
    val taulesXSessio = tXS.asScala.toArray
    val entityMeetings = convertirJavaAScala(mXP)
    val forbidden = convertirJavaAScala(fB)
    val sessioSlots = convertirJavaAScala(sS)
    val sessioMeetings = convertirJavaAScala(sM)
    val meetingSessions = convertirJavaAScala(mS)
    val meetingEntities = convertirJavaAScala(mP)
    val predef = pM.asScala.toArray

    val e = new ScalAT("/Users/adriaalabau/Projects/TFG/","/Users/adriaalabau/Projects/TFG/");

    //Variables del viewpoint
    val schedule = e.newVar2DArray(nMeetings, nSlots)

    //CONSTRAINT 1
    //Una entitat es pot trobar en reunions com a molt el seu nombre de participants
    for (p <- entityMeetings.indices; ts <- 0 until nSlots) {
      val list = (for (i <- entityMeetings(p)) yield (schedule(i)(ts))).toList
      e.addAMK(list, attendesXParticipan(p))
    }

    for (p <- predef) {
      e.addClause(schedule(p.meeting)(sessioSlots(p.sessio)(p.slot)) :: List())
    }

    //CONSTRAINT 2
    //Una entitat no te reunions en hores marcades que no assistira

    //REVISAR SI FORBIDEN ES CORRECTE
    for (entity <- entityMeetings.indices; forb <- forbidden(entity); meet <- entityMeetings(entity)) {
      e.addClause((-(schedule(meet)(forb))) :: List())
    }


    //CONSTRAINT 3  Afegim amb EO el llistat de tots els slots als quals la reunio pot tenir lloc. Neguem tots els contraris
    //CONSTRAINT 4
    //CONSTRAINT 5
    //Una reunio nomes es pot trobar en un de tots els seus slots disponibles
    for (meetingI <- meetingSessions.indices) {
      e.addEOQuad((for (ses <- meetingSessions(meetingI); slot <- sessioSlots(ses)) yield (schedule(meetingI)(slot))).toList)

      for (g2 <- 0 until nSessions if !meetingSessions(meetingI).contains(g2); slot <- sessioSlots(g2)) {
        e.addClause(-schedule(meetingI)(slot) :: List())
      }
    }

    //CONSTRAINT 6
    //Per cada slot de temps, les reunions de temps es poden colocar
    for (tSes <- 0 until nSessions; tSlot <- sessioSlots(tSes)) {
      e.addAMK((for (i <- sessioMeetings(tSes)) yield schedule(i)(tSlot)).toList, taulesXSessio(tSes))
      for(i <- sessioMeetings(tSes))
        System.out.println(schedule(i)(tSlot))
    }


    /*for (tSes <- 0 until nSessions; tSlot <- sessioSlots(tSes)) {
      val mida = 2
      (for (meet <- sessioMeetings(tSes) if mettingXParticipant(meet).size <= mida) yield meet).toList
    }*/

    var lRet = new java.util.ArrayList[java.util.ArrayList[java.util.ArrayList[Int]]]()
    //Solucionem
    if (e.solve()) {

      for (tSes <- 0 until nSessions) {
        var sessio = new java.util.ArrayList[java.util.ArrayList[Int]]()

        for (tSlot <- sessioSlots(tSes)) {
          var slot = new java.util.ArrayList[Int]()

          for (meet <- 0 until nMeetings) {
            if (e.getValue(schedule(meet)(tSlot)))
            {
              System.out.println(schedule(meet)(tSlot))
              slot.add(meet)
            }
          }
          sessio.add(slot)
        }
        lRet.add(sessio)
      }


      // for(j<- 0 to 8) {
      //   for (k <- 0 to 8)
      ///     if (e.getValue(graella(i)(j)(k))) print(k+1+" ")
      //   if(j%3==2) print(" ")
      // }
      //  println
      //  if(i%3==2) println
      // }

      //Printem estadistiques de solucio
      println("Solving time: " + e.getTime.toString + " seconds")
      println("N vars: " + e.getNVars)
      println("N clauses: " + e.getNClauses)
      println("N decisions: " + e.getDecisions)
      println("N propagations: " + e.getPropagations)
      println("N conflicts: " + e.getConflicts)


    }

    lRet
  }
}