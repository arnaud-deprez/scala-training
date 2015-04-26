package be.arndep.scala.fortheimpatient.exercices

/**
 * Created by Arnaud on 03-05-15.
 */
object Chapter14 extends App {
	// 1 TODO

	// 2
	{
		def swapPair(pair: (Int, Int)) = pair match {
			case (x, y) => (y, x)
		}
		println((1, 2))
	}

	// 3
	{
		def swapFirst2(a: Array[Int]) = a match {
			case Array(x, y, r @ _*) => Array(y, x) ++ r
			case _ => a
		}
		println(swapFirst2(Array(1)) mkString)
		println(swapFirst2(Array(1, 2)) mkString)
		println(swapFirst2(Array(1, 2, 3)) mkString)
	}

	// 4
	{
		abstract class Item
		case class Article(description: String, price: Double) extends Item
		case class Bundle(description: String, discount: Double, items: Item*) extends Item
		case class Multiple(count: Int, item: Item) extends Item

		def price(it: Item): Double = it match {
			case Article(_, p) => p
			case Bundle(_, disc, its @ _*) => its.map(price _).sum - disc
			case Multiple(count, item) => price(item) * count
		}

		val x = Bundle("Father's day special", 20.0,
			Multiple(2, Article("Scala for the Impatient", 39.95)),
			Bundle("Anchor Distillery Sampler", 10.0,
				Article("Old Potrero Straight Rye Whiskey", 79.95),
				Article("Junípero Gin", 32.95)
			)
		)

		println(price(x)) //162.8
	}

	// 5
	{
		//How you can visit List of Any and apply different rule for different match ! Awesome and simple !
		def sumLeaf(list: List[Any]) : Int = {
			(for(elem <- list) yield
				elem match {
					case x: List[Int] => sumLeaf(x)
					case x: Int => x
					case _ => 0
				}).sum
		}

		val x = List(List(3, 8), 2, List(5))

		println(x)
		println(sumLeaf(x))
	}

	// 6
	{
		//Even beter with object to represent the tree !
		sealed abstract class BinaryTree
		case class Leaf(value: Int) extends BinaryTree
		case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree

		def leafSum(tree: BinaryTree): Int = tree match {
			case Node(left, right) => leafSum(left) + leafSum(right)
			case Leaf(x) => x
		}

		val x = Node(Node(Leaf(3), Leaf(8)), Leaf(5)) //but the implementation doesn't allow to have 2
		println(x)
		println(leafSum(x))
	}

	// 7
	{
		sealed abstract class BinaryTree
		case class Leaf(value: Int) extends BinaryTree
		case class Node(leafs: BinaryTree*) extends BinaryTree // A node can contain many leafs

		def leafSum(tree: BinaryTree): Int = tree match {
			case Node(leafs @ _*) => leafs.map(leafSum _).sum
			case Leaf(x) => x
		}

		val x = Node(Node(Leaf(3), Leaf(8)), Leaf(2), Node(Leaf(5)))
		println(x)
		println(leafSum(x))
	}

	// 8
	{
		sealed abstract class BinaryTree
		case class Leaf(value: Int) extends BinaryTree
		case class Node(op: Char, leafs: BinaryTree*) extends BinaryTree // Contains operation to apply to its leaf

		def eval(tree: BinaryTree): Int = tree match {
			case Node(op, leafs @ _*) => op match {
				case '+' => leafs.map(eval _).sum
				case '-' => -leafs.map(eval _).sum
				case '*' => leafs.map(eval _).product
			}
			case Leaf(x) => x
		}

		val x = Node('+', Node('*', Leaf(3), Leaf(8)), Leaf(2),  Node('-', Leaf(5)))

		println(x)
		println(eval(x))
	}

	// 9
	{
		def sum(lst: List[Option[Int]]) = lst.map(_.getOrElse(0)).sum
		val x = List(Some(1), None, Some(2), None, Some(3))
		println(sum(x))
	}

	// 10
	{
		import math.sqrt

		type T = Double => Option[Double]

		def compose(f: T, g: T): T = {
			(x: Double) => g(x) match {
				case Some(x) => f(x)
				case None => None
			}
		}

		def f(x: Double) = if (x >= 0) Some(sqrt(x)) else None
		def g(x: Double) = if (x != 1) Some(1 / (x - 1)) else None
		val h = compose(f, g)

		println(h(2))
		println(h(1))
		println(h(0))
	}
}
