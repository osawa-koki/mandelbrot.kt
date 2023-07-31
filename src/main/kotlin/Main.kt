import org.w3c.dom.Element
import java.io.FileWriter
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

var basePath = "./public"

fun main(args: Array<String>) {
  try {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val document = builder.parse(Paths.get("./config.xml").toFile())
    val config = document.documentElement
    Mandelbrot.setBasePath(basePath)
    val mandelbrots: Array<Mandelbrot?> = configParse(config)
    val paths = arrayOfNulls<String>(mandelbrots.size)
    for (i in mandelbrots.indices) {
      val mandelbrot = mandelbrots[i] ?: continue
      mandelbrot.debugOutput()
      mandelbrot.draw()
      mandelbrot.save()
      paths[i] = mandelbrot.output
    }
    val pathsString = java.lang.String.join(",", *paths)
    val file = FileWriter(Paths.get(basePath, "paths.txt").toFile())
    file.write(pathsString)
    file.close()
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

fun configParse(config: Element): Array<Mandelbrot?> {
  val mandelbrots = config.getElementsByTagName("mandelbrot")
  val mandelbrotList: Array<Mandelbrot?> = arrayOfNulls(mandelbrots.length)
  for (i in 0 until mandelbrots.length) {
    val mandelbrot = mandelbrots.item(i) as Element
    val width = mandelbrot.getElementsByTagName("width").item(0).textContent.toInt()
    val height = mandelbrot.getElementsByTagName("height").item(0).textContent.toInt()
    val x_min = mandelbrot.getElementsByTagName("x_min").item(0).textContent.toDouble()
    val x_max = mandelbrot.getElementsByTagName("x_max").item(0).textContent.toDouble()
    val y_min = mandelbrot.getElementsByTagName("y_min").item(0).textContent.toDouble()
    val y_max = mandelbrot.getElementsByTagName("y_max").item(0).textContent.toDouble()
    val iteration = mandelbrot.getElementsByTagName("iteration").item(0).textContent.toInt()
    val incremental_step = mandelbrot.getElementsByTagName("incremental_step").item(0).textContent.toInt()
    val color_mode: ColorMode = ColorMode.valueOf(mandelbrot.getElementsByTagName("color_mode").item(0).textContent)
    val threshold = mandelbrot.getElementsByTagName("threshold").item(0).textContent.toInt()
    val output = mandelbrot.getElementsByTagName("output").item(0).textContent
    mandelbrotList[i] = Mandelbrot(
      width,
      height,
      x_min,
      x_max,
      y_min,
      y_max,
      iteration,
      incremental_step,
      color_mode,
      threshold,
      output
    )
  }
  return mandelbrotList
}
