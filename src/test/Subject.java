package test;

public interface Subject {
	public abstract void Notify();

	public abstract void Attach(Observer o);

	public abstract void Detach(Observer o);

}
