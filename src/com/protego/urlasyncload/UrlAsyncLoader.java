package com.protego.urlasyncload;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Dmitry Lekhtuz
 *
 */
public class UrlAsyncLoader {
	private String fileName;
	private int maxThreads = 2;

	private void run(String[] args) throws Exception {
		if (args.length > 0) {
			fileName = Objects.requireNonNull(args[0], "Missing file name");
		}
		
		if (args.length > 1) {
			maxThreads = Integer.valueOf(args[1]);
		}

		System.out.println("maxThreads " + maxThreads);
		System.out.println("fileName " + fileName);

		new ForkJoinPool(maxThreads)
			.submit(() -> {
					try {
						Files
							.lines(Path.of(fileName))
							.parallel()
							.map(UrlAsyncLoader.this::newUri)
							.map(HttpRequest::newBuilder)
							.map(Builder::build)
							.map(UrlAsyncLoader.this::getContent)
							.map(UrlAsyncLoader.this::md5)
							.forEach(System.out::println);
					} catch (IOException e) {
					}
			}
		)
		.get();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new UrlAsyncLoader().run(args);
	}

	/**
	 * @param uri
	 */
	private URI newUri(String uri) {
		try {
			System.out.println("URI: " + uri);
			return new URI(uri);
		} catch (Exception e) {
			return(null);
		}
	}

	/**
	 * @param req
	 */
	private byte[] getContent(HttpRequest req) {
		try {
			System.out.println("getContent: " + req.uri().toString());
			return HttpClient.newHttpClient().send(req, BodyHandlers.ofByteArray()).body();
		} catch (Exception e) {
			return(null);
		}
	}

	/**
	 * @param bytes
	 */
	private String md5(byte[] bytes) {
		try {
			return String.format("%032x", new BigInteger(1, MessageDigest.getInstance("MD5").digest(bytes)));
		} catch (Exception e) {
			return(null);
		}
	}
}
