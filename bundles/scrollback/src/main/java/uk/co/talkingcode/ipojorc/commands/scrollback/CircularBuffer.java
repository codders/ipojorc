package uk.co.talkingcode.ipojorc.commands.scrollback;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CircularBuffer<T> {

  protected Object[] storage;
  private int read;
  private int write;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  public CircularBuffer(int size)
  {
    storage = new Object[size];
    read = 0;
    write = 0;
  }
  
  public void put(T element)
  {
    lock.writeLock().lock();
    storage[write] = element;
    write = modInc(write);
    if (write == read)
      read = modInc(read);
    lock.writeLock().unlock();
  }
  
  public Iterator<T> borrowIterator()
  {
    lock.readLock().lock();
    return new BufferIterator<T>();
  }
  
  public void returnIterator()
  {
    lock.readLock().unlock();
  }

  protected int modInc(int value) {
    return (value + 1) % storage.length;
  }
  
  class BufferIterator<K> implements Iterator<T>
  {
    private int read;

    public BufferIterator(){
      this.read = CircularBuffer.this.read;
    }

    public boolean hasNext() {
      return read != CircularBuffer.this.write;
    }

    @SuppressWarnings("unchecked")
    public T next() {
      T result = (T) CircularBuffer.this.storage[read];
      read = CircularBuffer.this.modInc(read);
      return result;
    }

    public void remove() {
    }
  }
}
