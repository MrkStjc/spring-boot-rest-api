package spring.boot.application.restApi.tests.unit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import spring.boot.application.restApi.domain.AddBookResponse;
import spring.boot.application.restApi.domain.Library;
import spring.boot.application.restApi.controller.LibraryController;
import spring.boot.application.restApi.repository.LibraryRepository;
import spring.boot.application.restApi.service.LibraryService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
Naming convention is important, if we call class containing tests with Test or Tests, maven will consider
such tests as Unit tests. When we call class ending with IT it will be considered as integration tests.
 */

@SpringBootTest
@AutoConfigureMockMvc
@Feature("LibraryControllerUnitTests")
class RestApiApplicationTests {

	@Autowired
	private LibraryService libraryService;

	@MockBean
	private LibraryService mockLibraryService;

	@MockBean
	private LibraryRepository mockLibraryRepository;

	@Autowired
	private LibraryController libraryController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static Stream<Arguments> buildIdTestData() {
		return Stream.of(
						Arguments.of("ZEMUN", 84, "OLDZEMUN84"),
						Arguments.of("EMUN", 84, "EMUN84"),
						Arguments.of("", 84, "84")
		);
	}

	private static Stream<Arguments> addBookStatusCodeTestData() {
		return Stream.of(
						Arguments.of(false, HttpStatus.CREATED),
						Arguments.of(true, HttpStatus.ACCEPTED)
		);
	}

	@Test
	void contextLoads() {
	}

	// TODO: Set data driven tests!

//	@ParameterizedTest
//	@MethodSource("buildIdTestData")
//	public void buildIdTest(String isbn, Integer aisle, String expectedId) {
//		String actualId = libraryService.buildId(isbn, aisle);
//		assertEquals(actualId, expectedId);
//	}

	@ParameterizedTest
	@DisplayName("Add book status code test")
	@MethodSource("addBookStatusCodeTestData")
	public void addBookStatusCodeTest(Boolean bookExists, HttpStatus expStatus) {
		Library library = buildLibrary();

		// Mocks

		when(libraryService.doesBookExist(library.getId())).thenReturn(bookExists);
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		when(mockLibraryRepository.save(any())).thenReturn(library);
		ResponseEntity response = libraryController.addBook(library);
		assertEquals(expStatus, response.getStatusCode());

	}

	// Unit testing when invoking methods from controller directly.

	@Test
	public void addBookResponseTypeTest() {
		Library library = buildLibrary();

		// Mocks

		when(libraryService.doesBookExist(library.getId())).thenReturn(false);
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		when(mockLibraryRepository.save(any())).thenReturn(library);
		ResponseEntity response = libraryController.addBook(library);
		assertThat(response.getBody()).isInstanceOf(AddBookResponse.class);

	}

	@Test
	public void addBookResponseMsgTest() {
		Library library = buildLibrary();

		// Mocks

		when(libraryService.doesBookExist(library.getId())).thenReturn(false);
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		when(mockLibraryRepository.save(any())).thenReturn(library);
		ResponseEntity response = libraryController.addBook(library);
		AddBookResponse expectedResponse = (AddBookResponse) response.getBody();
		assertThat(expectedResponse.getMsg()).isEqualTo("Success book is added!");

	}

	// Unit testing when calling endpoint from controller using MockMvc in serverless mode - no need for starting server - we call service from code directly.
	// Responses are in core JSON - it checks also spring boot serialization / deserialization. Approach from above is using classes itself.
	// Somewhere between unit and integration testing.
	// It is the best for testing controller methods!
	// Only method under test invocation is different.
	// So MockMVC is used to mock service call internally..

	@Test
	public void addBookControllerUnitTest() throws Exception {
		Library library = buildLibrary();

		// Mocks
		String requestJson = objectMapper.writeValueAsString(library);
		when(libraryService.doesBookExist(library.getId())).thenReturn(false);
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		when(mockLibraryRepository.save(any())).thenReturn(library);
		mockMvc.perform(post("/api/library/addBook")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
						.andDo(print()) // Prints response in console
						.andExpect(status().isCreated()) // Status code validation
						.andExpect(jsonPath("$.id").value(library.getId())); // Response json id value
	}

	@Test
	public void getBookByAuthorTest() throws Exception {
		List<Library> libraryList = new ArrayList<>();
		libraryList.add(buildLibrary());
		libraryList.add(buildLibrary());
		when(mockLibraryRepository.findAllBooksByAuthor(any())).thenReturn(libraryList);
		mockMvc.perform(get("/api/library/getBooks/author")
						.param("authorname", "Mirko"))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.length()", is(2))) // To check length of returned array
						.andExpect(jsonPath("$.[0].author").value(libraryList.get(0).getAuthor())); // Verify 1st member of array
	}

	// TODO: Scenario when optional is empty!
	@Test
	public void updateBookTest() throws Exception {
		Library expectedValues = buildLibrary();
		Library library = buildLibrary();
		Optional<Library> optionalLibrary = Optional.of(library);
		Library updateLibrary = updateLibrary();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String requestJson = objectMapper.writeValueAsString(updateLibrary);
		when(mockLibraryRepository.findById(any())).thenReturn(optionalLibrary); // NOTE: This mock actually changes values of optionalLibrary object - through setters in update book method!
		when(mockLibraryRepository.save(library)).thenReturn(library); // Maybe no need in mocking this repo method since we have repository as MockBean and it won't interact with Db.
		mockMvc.perform(put(String.format("/api/library/updateBook/%s", library.getId()))
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
						.andExpect(status().isOk())
				// TODO: Check how to validate entire response content:		.andExpect(content().json("{\"book_name\":\"UpdateBookName\",\"id\":\"OLDZEMUN666\",\"isbn\":\"ZEMUN\",\"aisle\":999,\"author\":\"UpdateAuthor\"}"));
						.andExpect(jsonPath("$.author").value(updateLibrary.getAuthor()))
						.andExpect(jsonPath("$.book_name").value(updateLibrary.getBook_name()))
						.andExpect(jsonPath("$.aisle").value(updateLibrary.getAisle()));
	}

	@Test
	public void updateBookTestNonExistingId() throws Exception {
		Library library = buildLibrary();
		Optional<Library> optionalLibrary = Optional.empty();
		Library updateLibrary = updateLibrary();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String requestJson = objectMapper.writeValueAsString(updateLibrary);
		when(mockLibraryRepository.findById(any())).thenReturn(optionalLibrary);
		mockMvc.perform(put(String.format("/api/library/updateBook/%s", library.getId()))
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
						.andExpect(status().isNotFound());
	}

	@Test
	public void deleteBookControllerTest() throws Exception {
		Library library = buildLibrary();
		Optional<Library> maybeLibrary = Optional.of(library);
		String requestJson = objectMapper.writeValueAsString(library);
		when(mockLibraryRepository.findById(any())).thenReturn(maybeLibrary);
		doNothing().when(mockLibraryRepository).delete(library); // Mocking void return methods!
		MvcResult response = mockMvc.perform(delete("/api/library/deleteBook")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
						.andExpect(status().isOk())
						.andReturn();
		assertThat(response.getResponse().getContentAsString()).isEqualTo("Book is deleted!");
	}



	private Library buildLibrary() {
		Library lib = new Library();
		lib.setAuthor("Mirko");
		lib.setBook_name("Testonja");
		lib.setAisle(666);
		lib.setIsbn("ZEMUN");
		lib.setId("OLDZEMUN666");
		return lib;
	}

	private Library updateLibrary() {
		Library lib = new Library();
		lib.setAuthor("UpdateAuthor");
		lib.setBook_name("UpdateBookName");
		lib.setAisle(999);
		return lib;
	}

}
