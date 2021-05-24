package Server;

public interface ClientResponseCallback {

	void stringMessageReceived(String string);

	void objectMessageReceived(Object object);
}
