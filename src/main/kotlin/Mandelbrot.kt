import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

enum class ColorMode {
  RED,
  GREEN,
  BLUE,
  WHITE
}

class Mandelbrot(
  var width: Int,
  var height: Int,
  var x_min: Double,
  var x_max: Double,
  var y_min: Double,
  var y_max: Double,
  var iteration: Int,
  var incremental_step: Int,
  var color_mode: ColorMode,
  var threshold: Int,
  output: String
) {
  var output: String
  var image: Image

  init {
    this.output = output.replace("#{GUID}", UUID.randomUUID().toString())
    image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
  }

  fun debugOutput() {
    println("width: $width")
    println("height: $height")
    println("x_min: $x_min")
    println("x_max: $x_max")
    println("y_min: $y_min")
    println("y_max: $y_max")
    println("iteration: $iteration")
    println("incremental_step: $incremental_step")
    println("color_mode: $color_mode")
    println("threshold: $threshold")
    println("base_path: " + base_path)
    println("output: $output")
  }

  fun draw() {
    val graphics = image.graphics
    for (x in 0 until width) {
      for (y in 0 until height) {
        val zx = x_min + (x_max - x_min) * x / width
        val zy = y_min + (y_max - y_min) * y / height
        var z = Complex(0.0, 0.0)
        var i = 0
        var _color: Int
        while (z.abs() < threshold && i < iteration) {
          z = z.mul(z).add(Complex(zx, zy))
          i++
        }
        _color = if (i == iteration) {
          Rgb2Int(0, 0, 0)
        } else {
          when (color_mode) {
            ColorMode.RED -> Rgb2Int(255, 255 - i * incremental_step, 255 - i * incremental_step)
            ColorMode.GREEN -> Rgb2Int(255 - i * incremental_step, 255, 255 - i * incremental_step)
            ColorMode.BLUE -> Rgb2Int(255 - i * incremental_step, 255 - i * incremental_step, 255)
            else -> throw IllegalStateException("Unexpected value: $color_mode")
          }
        }
        graphics.color = Color(_color)
        graphics.fillRect(x, y, 1, 1)
      }
    }
  }

  fun save() {
    try {
      ImageIO.write(image as BufferedImage, "png", Paths.get(base_path, output).toFile())
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun Rgb2Int(r: Int, g: Int, b: Int): Int {
    var r = r
    var g = g
    var b = b
    r = r % 255
    g = g % 255
    b = b % 255
    return (r shl 16) + (g shl 8) + b
  }

  companion object {
    var base_path = ""
    fun setBasePath(base_path: String) {
      Companion.base_path = base_path
    }
  }
}
