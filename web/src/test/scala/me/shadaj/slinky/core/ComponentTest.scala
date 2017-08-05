package me.shadaj.slinky.core

import me.shadaj.slinky.core.facade.ComponentInstance
import me.shadaj.slinky.web.ReactDOM
import org.scalajs.dom
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.Promise
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object TestComponent extends Component {
  type Props = Int => Unit
  type State = Int

  @ScalaJSDefined
  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState: Int = 0

    override def componentWillUpdate(nextProps: Props, nextState: Int): Unit = {
      props.apply(nextState)
    }

    override def componentDidMount(): Unit = {
      setState((s, p) => {
        s + 1
      })
    }

    override def render(): ComponentInstance = {
      null
    }
  }
}

class ComponentTest extends AsyncFunSuite {
  test("setState given function is applied") {
    val promise: Promise[Assertion] = Promise()

    ReactDOM.render(
      TestComponent(i => promise.success(assert(i == 1))),
      dom.document.createElement("div")
    )

    promise.future
  }
}