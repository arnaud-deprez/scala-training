import be.arndep.scala.coursera.week7.Pouring

val problem = new Pouring(Vector(4, 7))

problem.moves

problem.pathSets take 3 toList

problem.solutions(6)