package commons.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * created_by:  MONGOUtil
 * created_at:  2019/3/13 9:25
 * version:     1.0
 * description:
 * records of modification: [20190214]
 */
public class MongoUtils {


    @SuppressWarnings("SubscriberImplementation")
    public static class MongoBasicSubscriber<T> implements Subscriber<T> {

        private final List<T> received;
        private final List<Throwable> errors;
        private final CountDownLatch latch;
        private volatile Subscription subscription;
        private volatile boolean completed;

        MongoBasicSubscriber() {
            this.received = new ArrayList<>();
            this.errors = new ArrayList<>();
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
        }

        @Override
        public void onNext(T t) {
            this.received.add(t);
        }

        @Override
        public void onError(Throwable t) {
            this.errors.add(t);
            onComplete();
        }

        @Override
        public void onComplete() {
            completed = true;
            latch.countDown();
//            System.out.printf("Completed %d operations!\n", received.size());
        }

        public Subscription getSubscription() {
            return this.subscription;
        }

        public List<T> getReceived() {
            return received;
        }

        public Throwable getErrors() {
            if (this.errors.size() > 0) {
                return this.errors.get(0);
            }
            return null;
        }

        private boolean isCompleted() {
            return this.completed;
        }

        public List<T> get(final long timeout, final TimeUnit unit) throws Throwable {
            return await(timeout, unit).getReceived();
        }

        public MongoBasicSubscriber<T> await() throws Throwable {
            return await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        }

        public MongoBasicSubscriber<T> await(final long timeout, final TimeUnit unit) throws Throwable {
            subscription.request(Integer.MAX_VALUE);
            if (!latch.await(timeout, unit)) {
                throw new MongoTimeoutException("Publisher onComplete timed out!");
            }
            if (!errors.isEmpty()) {
                throw errors.get(0);
            }
            return this;
        }
    }

    public static class MongoOperationSubscriber<T> extends MongoBasicSubscriber<T> {
        @Override
        public void onSubscribe(Subscription s) {
            super.onSubscribe(s);
            s.request(Integer.MAX_VALUE);
        }
    }

    public static class MongoPrintDocumentSubscriber extends MongoBasicSubscriber<Document> {
        @Override
        public void onNext(final Document document) {
            super.onNext(document);
//            System.out.println(document.toJson());
        }
    }


    private MongoClient mongoClient = null;
    private MongoClientSettings mongoClientSettings;

    public MongoUtils(String dbUser, String pass, String ips, String authSource) {
        // 配置授权
        char[] dbPass = new char[pass.length()];
        pass.getChars(0, dbPass.length, dbPass, 0);
        MongoCredential mongoCredential = MongoCredential.createCredential(dbUser, authSource, dbPass);
        String[] tmp = ips.split(":");
        List<ServerAddress> serverAddresses = new ArrayList<>();
        serverAddresses.add(new ServerAddress(tmp[0], Integer.valueOf(tmp[1])));
        this.mongoClientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(serverAddresses))
                .credential(mongoCredential)
                .build();
    }

    public MongoUtils(String dbUser, String pass, String[] ips, String authSource) {
        // 配置授权
        char[] dbPass = new char[pass.length()];
        pass.getChars(0, dbPass.length, dbPass, 0);
        MongoCredential mongoCredential = MongoCredential.createCredential(dbUser, authSource, dbPass);

        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String ipAndPort :
                ips) {
            String[] tmp = ipAndPort.split(":");
            serverAddresses.add(new ServerAddress(tmp[0], Integer.valueOf(tmp[1])));
        }
        this.mongoClientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(serverAddresses))
                .credential(mongoCredential)
                .build();
    }

    private MongoClient getMongoClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(mongoClientSettings);
        }
        return this.mongoClient;
    }

    private MongoCollection<Document> getMongoCollection(String dbName, String collectionName) {
        MongoClient mongoClient = getMongoClient();
        return mongoClient.getDatabase(dbName).getCollection(collectionName);
    }


    public void insertOne(String dbName, String collectionName, Document document) {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        mongoCollection.insertOne(document).subscribe(new MongoOperationSubscriber<>());

    }

    public void insertMany(String dbName, String collectionName, List<Document> documents) throws Throwable {
        Log.debug(dbName+"  "+collectionName);
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoOperationSubscriber<Success> subscriber = new MongoOperationSubscriber<>();
        mongoCollection.insertMany(documents).subscribe(subscriber);
        subscriber.await();
        Log.debug(subscriber.getReceived());
    }

    public List<Document> query(String dbName, String collectionName, Bson filter, int limit) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoPrintDocumentSubscriber subscriber = new MongoPrintDocumentSubscriber();
        mongoCollection.find(filter).limit(limit).subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived();
    }


    public List<Document> query(String dbName, String collectionName, Bson filter) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoPrintDocumentSubscriber subscriber = new MongoPrintDocumentSubscriber();
        mongoCollection.find(filter).subscribe(subscriber);
        subscriber.await();
        System.out.println(subscriber.getReceived());
        return subscriber.getReceived();
    }

    public boolean include(String dbName, String collectionName, Bson filter) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoPrintDocumentSubscriber subscriber = new MongoPrintDocumentSubscriber();
        mongoCollection.find(filter).subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived().size()!=0;
    }



    public List<Document> queryAll(String dbName, String collectionName) throws Throwable {
        Log.debug(dbName+"  "+collectionName);
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoPrintDocumentSubscriber subscriber = new MongoPrintDocumentSubscriber();
        mongoCollection.find().subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived();
    }

    // 该函数可过滤多于的属性
    public List<Document> queryAndProjection(String dbName, String collectionName, Bson filter, Bson projection) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoPrintDocumentSubscriber subscriber = new MongoPrintDocumentSubscriber();
        mongoCollection.find(filter).projection(projection).subscribe(subscriber);
        subscriber.await();
        return subscriber.getReceived();
    }

    // 虽然有多个匹配，但只更新第一个
    public void updateOne(String dbName, String collectionName, Bson filter, Document document) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoOperationSubscriber<UpdateResult> subscriber = new MongoOperationSubscriber<>();
        mongoCollection.updateOne(filter, document).subscribe(subscriber);
    }

    public void updateMany(String dbName, String collectionName, Bson filter, Document document) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoOperationSubscriber<UpdateResult> subscriber = new MongoOperationSubscriber<>();
        mongoCollection.updateMany(filter, document).subscribe(subscriber);
    }

    // 虽然有多个匹配，但只删除第一个
    public void deleteOne(String dbName, String collectionName, Bson filter) throws Throwable {
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoOperationSubscriber<DeleteResult> subscriber = new MongoOperationSubscriber<>();
        mongoCollection.deleteOne(filter).subscribe(subscriber);
        subscriber.await();
    }


    public void deleteMany(String dbName, String collectionName, Bson filter) throws Throwable {
        Log.debug(dbName+"  "+collectionName);
        MongoCollection<Document> mongoCollection = getMongoCollection(dbName, collectionName);
        MongoOperationSubscriber<DeleteResult> subscriber = new MongoOperationSubscriber<>();
        mongoCollection.deleteMany(filter).subscribe(subscriber);
        subscriber.await();
        Log.debug(subscriber.getReceived());
    }

    @Override
    public void finalize() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
