package org.grails.plugins.elasticsearch.conversion.unmarshall

class DefaultUnmarshallingContext {
  Stack unmarshallingStack = new Stack()
  Stack cycleRefStack = new Stack()

  def addCycleRef(data) {
    assert data.ref
    def referredPos = unmarshallingStack.size() - (data.ref.split('/').size()+1)
    def sourcePath = ''
    if(referredPos >= 0){
      sourcePath = unmarshallingStack[0..referredPos].join('/')
    }
    def cycleRef = new CycleReferenceSource(
            cyclePath:unmarshallingStack.join('/'),
            sourcePath:sourcePath
    )
    cycleRefStack.push(cycleRef)
  }

  def resetContext() {
    unmarshallingStack.clear()
    cycleRefStack.clear()
  }
}
