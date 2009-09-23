package it.assist.jrecordbind;

public interface Evaluator<T, S> {

  public void eval(T target, S source);

}
