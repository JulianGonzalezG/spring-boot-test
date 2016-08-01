package com.vector.itc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("/file")
@EnableAutoConfiguration
public class RestMassiveCpApplication {

	@RequestMapping(value = "/")
	public ResponseEntity<RequestJson> get() {

		RequestJson rq = new RequestJson();
		rq.setNewRoute("NEW ROUTE");
		rq.setOriginalRoute("FOLDER TO COPY");
		rq.setConfigTxt("CONFIG TXT");

		return new ResponseEntity<RequestJson>(rq, HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseJson> list(@RequestBody RequestJson request) {

		List<String> fileList = new ArrayList<>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(request.getOriginalRoute()))) {
			for (Path path : directoryStream) {
				if(path.getFileName().endsWith(".java")) {
					fileList.add(path.toString());
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}



		ResponseJson response = new ResponseJson();
		response.setNoCopiados(fileList);

		return new ResponseEntity<ResponseJson>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/copy", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseJson> copy(@RequestBody RequestJson request){
		int i = 0;
		ResponseJson response = new ResponseJson();
		List<String> fileList = new ArrayList<>();
		List<String> noCopyList = new ArrayList<>();

		if(request != null) {
			Path path = Paths.get(request.getConfigTxt());
			//Path path = Paths.get("C:/Users/jgonzalezg/Desktop/clases booking.txt");
			try (BufferedReader br = Files.newBufferedReader(path)) {
				fileList = br.lines().collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String file : fileList) {
				//String rutaOriginal = "C:/Users/jgonzalezg/Documents/apps-front/enterprise-data/src" + file;
				//String rutaNueva = "C:/Users/jgonzalezg/Documents/booking/enterprise-data/src" + file;
				String rutaOriginal = request.getOriginalRoute() + file;
				String rutaNueva = request.getNewRoute() + file;

				Path sourcePath = Paths.get(rutaOriginal);
				Path destinationPath = Paths.get(rutaNueva);

				if (Files.exists(sourcePath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
					if (Files.exists(destinationPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
						try {
							Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (FileAlreadyExistsException e) {
							//destination file already exists
						} catch (IOException e) {
							//something else went wrong
							e.printStackTrace();
						}
						i++;
					} else {
						try {
							Files.createDirectories(destinationPath.getParent());
							Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
						i++;
					}
				} else {
					noCopyList.add(rutaOriginal);
				}
			}
		}
		response.setConfigTxt(request.getConfigTxt());
		response.setOriginalRoute(request.getOriginalRoute());
		response.setNewRoute(request.getNewRoute());
		response.setTotalCopiados(i);
		response.setTotalAcopiar(fileList.size());
		response.setNoCopiados(noCopyList);
		return new ResponseEntity<ResponseJson>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/merge", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseJson> merge(@RequestBody RequestMergeJson request){
		int i = 0;
		ResponseJson response = new ResponseJson();
		List<String> fileList = new ArrayList<>();
		List<String> noCopyList = new ArrayList<>();

		if(request != null) {
			Path path = Paths.get(request.getConfigTxt());
			//Path path = Paths.get("C:/Users/jgonzalezg/Desktop/clases booking.txt");
			try (BufferedReader br = Files.newBufferedReader(path)) {
				fileList = br.lines().collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String file : fileList) {
				//String rutaOriginal = "C:/Users/jgonzalezg/Documents/apps-front/enterprise-data/src" + file;
				//String rutaNueva = "C:/Users/jgonzalezg/Documents/booking/enterprise-data/src" + file;
				String rutaOriginal = request.getOriginalRoute() + file;
				String rutaNueva = request.getNewRoute() + file;
				String rutaBusqueda = request.getSearchRoute();

				Path sourcePath = Paths.get(rutaOriginal);
				Path searchPath = Paths.get(rutaBusqueda);
				String globSearch = "glob:**/"+sourcePath.getFileName().toString();
				try {
					response = match(globSearch, rutaBusqueda, rutaNueva, response);
				} catch (IOException e) {
					e.printStackTrace();
				}

				/*if (Files.exists(sourcePath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
					if (Files.exists(destinationPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
						try {
							Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (FileAlreadyExistsException e) {
							//destination file already exists
						} catch (IOException e) {
							//something else went wrong
							e.printStackTrace();
						}
						i++;
					} else {
						try {
							Files.createDirectories(destinationPath.getParent());
							Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
						i++;
					}
				} else {
					noCopyList.add(rutaOriginal);
				}*/
			}
		}
		response.setConfigTxt(request.getConfigTxt());
		response.setOriginalRoute(request.getOriginalRoute());
		response.setNewRoute(request.getNewRoute());
		//response.setTotalCopiados(i);
		response.setTotalAcopiar(fileList.size());
		response.setNoCopiados(noCopyList);
		return new ResponseEntity<ResponseJson>(response, HttpStatus.OK);
	}

	public ResponseJson match(String glob, String location, String newLocation,ResponseJson response) throws IOException {

		final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(
				glob);

		Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path path,
											 BasicFileAttributes attrs) throws IOException {
				if (pathMatcher.matches(path)) {
					System.out.println(path);
					Path destinationPath = Paths.get(newLocation);
					if (Files.exists(destinationPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
						try {
							Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (FileAlreadyExistsException e) {
							//destination file already exists
						} catch (IOException e) {
							//something else went wrong
							e.printStackTrace();
						}
						response.setTotalCopiados(response.getTotalCopiados() + 1);
					} else {
						try {
							Files.createDirectories(destinationPath.getParent());
							Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							e.printStackTrace();
						}
						response.setTotalCopiados(response.getTotalCopiados() + 1);
					}

					return FileVisitResult.TERMINATE;
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		return response;
	}

	public static void main(String[] args) {
		SpringApplication.run(RestMassiveCpApplication.class, args);
	}
}
