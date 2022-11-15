package ru.javawebinar.basejava.storage;

public class ObjectStreamStorage {
//        extends FileStorage {
//
//    protected ObjectStreamStorage(File directory) {
//        super(directory);
//    }
//
//    @Override
//    protected void doWrite(Resume r, OutputStream outputStream) throws IOException {
//        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
//            objectOutputStream.writeObject(r);
//        }
//    }
//
//    @Override
//    protected Resume doRead(InputStream inputStream) throws IOException {
//        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
//            return (Resume) objectInputStream.readObject();
//        } catch (ClassNotFoundException e) {
//            throw new StorageException("Error read resume", null, e);
//        }
//    }
}
