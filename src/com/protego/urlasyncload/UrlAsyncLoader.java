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
import java.util.concurrent.ForkJoinPool;

/**
 * @author Dmitry Lekhtuz
 *
 */
public class UrlAsyncLoader {
	private String fileName;
	private int maxThreads = 2;

	private void run(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Missing input file name");
			return;
		}

		fileName = args[0];
		
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
							.map(UrlAsyncLoader.this::calculateDigest)
							.forEach(System.out::println);
					} catch (IOException e) {
						System.out.println("Input file does not exist.");
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
			System.out.println("URL is in invalid format.");
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
			System.out.println("Error sending HTTP request.");
			return(null);
		}
	}

	/**
	 * @param bytes
	 */
	private String calculateDigest(byte[] bytes) {
		try {
			return String.format("%032x", 
					new BigInteger(1, 
							MessageDigest.getInstance("MD5").digest(bytes)));
		} catch (Exception e) {
			System.out.println("Unable to calculate MD5 hash.");
			return(null);
		}
	}
}
