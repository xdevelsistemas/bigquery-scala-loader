object m {
  val a = Map("1"->"a", "2"->"b", "3"->"c")
  a.foreach(x=>x._1 + x._2)
}