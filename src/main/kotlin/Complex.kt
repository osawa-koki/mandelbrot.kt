package org.example

data class Complex(val re: Double, val im: Double) {
  fun add(c: Complex): Complex {
    return Complex(re + c.re, im + c.im)
  }

  fun sub(c: Complex): Complex {
    return Complex(re - c.re, im - c.im)
  }

  fun mul(c: Complex): Complex {
    return Complex(
      re * c.re - im * c.im, re * c.im + im * c.re
    )
  }

  operator fun div(c: Complex): Complex {
    val denominator = c.re * c.re + c.im * c.im
    return Complex(
      (re * c.re + im * c.im) / denominator,
      (im * c.re - re * c.im) / denominator
    )
  }

  fun Con(): Complex {
    return Complex(re, -im)
  }

  fun abs(): Double {
    return Math.sqrt(re * re + im * im)
  }

  fun arg(): Double {
    return Math.atan(im / re)
  }

  override fun toString(): String {
    return if (im >= 0) "(" + re + " + " + im + "i" + ")" else "(" + re + " - " + -im + "i" + ")"
  }
}
