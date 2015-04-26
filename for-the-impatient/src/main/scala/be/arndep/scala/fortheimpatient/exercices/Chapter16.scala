package be.arndep.scala.fortheimpatient.exercices

import scala.xml._
import scala.xml.parsing.XhtmlParser
import scala.xml.transform.{RewriteRule, RuleTransformer}

/**
 * Created by Arnaud on 10-05-15.
 */
object Chapter16 extends App {
	// 1
	{
		println(<fred/>(0))
		println(<fred/>(0)(0))
		// <fred/> return a NodeSeq of one xml element <fred/>,
		// <fred/>(0) return the first element of the NodeSeq (which is <fred/> and it's also a NodeSeq)
		// <fred/>(0)(0) return the first element of the NodeSeq (which is <fred/>) and then again the first element of this NodeSeq
	}

	// 2
	{
		// double braces to avoid conflict
		val list = <ul>
			<li>Opening bracket: [</li>
			<li>Closing bracket: ]</li>
			<li>Opening brace: {{</li>
			<li>Closing brace: }}</li>
		</ul>
		println(list)
	}

	// 3
	{
		val v1 = <li>Fred</li> match { case <li>{Text(t)}</li> => t }
		println(v1 + ": " + v1.getClass) //String
		val v2 = <li>{"Fred"}</li> match { case <li>{t: Atom[_]}</li> => t }
		println(v2 + ": " + v2.getClass) //Atom[String]
	}

	// 4
	{
		def printImgsWithoutAlt(filename: String) {
			val parser = new XhtmlParser(io.Source.fromFile(filename))
			val root = parser.initialize.document.docElem
			val imgs = root \\ "img"
			for (img <- imgs if img.attributes.get("alt") == None) {
				println(img)
			}
		}

		println("ex 4")
		printImgsWithoutAlt("files/placekitten.com.html")
	}

	// 5
	{
		def printSrcOfImgs(filename: String) {
			val parser = new XhtmlParser(io.Source.fromFile(filename))
			val root = parser.initialize.document.docElem
			val imgs = root \\ "img"
			for (img <- imgs if img.attributes.get("src") != None) {
				println(img.attributes("src"))
			}
		}

		println("ex 5")
		printSrcOfImgs("files/placekitten.com.html")
	}

	// 6
	{
		def printLinks(filename: String) {
			val parser = new XhtmlParser(io.Source.fromFile(filename))
			val root = parser.initialize.document.docElem
			val anchors = root \\ "a"

			def toText(n: Node) = n match {
				case x: Text => x.toString
				case _ => ""
			}

			for (anchor <- anchors if anchor.attributes.get("href") != None) {
				val text = anchor.child.map(toText).reduceLeft(_ + _)
				println(text + "\t" + anchor.attributes("href"))
			}
		}
		println("ex 6")
		printLinks("files/placekitten.com.html")
	}

	// 7
	{
		def makeHtmlDl(map: Map[String, String]): Elem = {
			val items = for (i <- map)
				yield { <xml:group><dt>{i._1}</dt><dd>{i._2}</dd></xml:group> }
			<dl>{items}</dl>
		}
		println("ex 7")
		println(makeHtmlDl(Map[String, String]("A" -> "1", "B" -> "2", "C" -> "3")))
	}

	// 8
	{
		def makeMapFromDl(dl: Elem): Map[String, String] = {
			val dts = (dl \ "dt").map(_.child(0).toString)
			val dds = (dl \ "dd").map(_.child(0).toString)
			(dts zip dds).toMap
		}
		println("ex 8")
		println(makeMapFromDl(<dl><dt>A</dt><dd>1</dd><dt>B</dt><dd>2</dd><dt>C</dt><dd>3</dd></dl>))
	}

	// 9 & 10
	{
		def addAlt(filename: String): Seq[Node] = {
			val parser = new XhtmlParser(io.Source.fromFile(filename))
			val root = parser.initialize.document.docElem
			val anchors = root \\ "a"

			val rewriteRule = new RewriteRule {
				override def transform(n: Node): Seq[Node] = n match {
					case x @ <img>{_*}</img> if x.attributes.get("alt") == None =>
						x.asInstanceOf[Elem] % Attribute(null, "alt", "TODO", scala.xml.Null)
					case _ => n
				}
			}

			new RuleTransformer(rewriteRule).transform(root)
		}

		val transformed = addAlt("files/placekitten.com.html")
		println(transformed.head)

		// 10 save it
		XML.save("target/out.xml", transformed.head, enc = "UTF-8", xmlDecl = false)
	}
}
