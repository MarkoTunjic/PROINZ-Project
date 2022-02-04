package hr.fer.proinz.skilletcooking.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import javax.transaction.Transactional;

import com.google.gson.Gson;

import org.springframework.web.multipart.MultipartFile;

import hr.fer.proinz.skilletcooking.controllers.RecipeController.RecipeData;
import hr.fer.proinz.skilletcooking.models.User;
import hr.fer.proinz.skilletcooking.payload.Request.RecipeRequest;
import hr.fer.proinz.skilletcooking.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Test
        void testPostDeniedWhenLoggedIn() throws Exception {
                mockMvc.perform(post("/api/recipes/")).andExpect(status().is4xxClientError());
        }

        @Test
        void testGetNewRecipeDataWhenPassingNoData() {
                assertThrows(IllegalArgumentException.class,
                                () -> RecipeController.getNewRecipeData(new RecipeRequest(), new User()));
        }

        @Test
        void testGetNewRecipeDataWhenPassingValidData() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour", "Milk" });
                request.setQuantity(new int[] { 1, 2 });
                request.setMeasure(new String[] { "kg", "l" });
                request.setUserId(1);
                request.setPictures(
                                new MultipartFile[] {
                                                new MockMultipartFile("keksi", "keksi.png", MediaType.IMAGE_PNG_VALUE,
                                                                this.getClass().getResourceAsStream("images/keksi.png")
                                                                                .readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                RecipeData data = RecipeController.getNewRecipeData(request, user);
                assertNotNull(data);
                assertEquals(data.getRecipe().getTitle(), "New");
                assertEquals(data.getRecipe().getUser(), user);
                assertEquals(true,
                                data.getIngredients().stream().allMatch(
                                                ingredient -> ingredient.getRecipe().equals(data.getRecipe())));
                assertEquals(true,
                                data.getImages().stream()
                                                .allMatch(image -> image.getRecipe().equals(data.getRecipe())));
                assertEquals(true,
                                data.getSteps().stream().allMatch(step -> step.getRecipe().equals(data.getRecipe())));
        }

        @Test
        void testGetNewRecipeDataWhenPassingTooManyImages() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                MultipartFile[] pictures = new MultipartFile[6];
                for (int i = 0; i < pictures.length; i++)
                        pictures[i] = new MockMultipartFile("keksi", "keksi.png", MediaType.IMAGE_PNG_VALUE,
                                        this.getClass().getResourceAsStream("images/keksi.png").readAllBytes());
                request.setPictures(pictures);
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenPassingMaximumAmountOfImages() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                MultipartFile[] pictures = new MultipartFile[5];
                for (int i = 0; i < pictures.length; i++)
                        pictures[i] = new MockMultipartFile("keksi", "keksi.png", MediaType.IMAGE_PNG_VALUE,
                                        this.getClass().getResourceAsStream("images/keksi.png").readAllBytes());
                request.setPictures(pictures);
                User user = userRepository.findById(request.getUserId()).get();
                RecipeData data = RecipeController.getNewRecipeData(request, user);
                assertNotNull(data);
                assertEquals(data.getRecipe().getTitle(), "New");
                assertEquals(data.getRecipe().getUser(), user);
                assertEquals(true,
                                data.getIngredients().stream().allMatch(
                                                ingredient -> ingredient.getRecipe().equals(data.getRecipe())));
                assertEquals(true,
                                data.getImages().stream()
                                                .allMatch(image -> image.getRecipe().equals(data.getRecipe())));
                assertEquals(true,
                                data.getSteps().stream().allMatch(step -> step.getRecipe().equals(data.getRecipe())));
                assertEquals(5, data.getImages().size());
        }

        @Test
        void testGetNewRecipeDataWhenPassingEmptyImages() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] {});
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenNotPassingImages() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenNotPassingTitle() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenPassingEmptyTitle() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenNotPassingSteps() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenPassingEmptyStep() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenNotPassingIngredients() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenPassingIngredientWithNoName() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenPassingIngredientWithNoQuantity() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] {});
                request.setMeasure(new String[] { "kg" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenPassingIngredientWithNoMeasure() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "" });
                request.setUserId(1);
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(request.getUserId()).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        void testGetNewRecipeDataWhenNotPassingUserId() throws IOException {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "" });
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                User user = userRepository.findById(1).get();
                assertThrows(IllegalArgumentException.class, () -> RecipeController.getNewRecipeData(request, user));
        }

        @Test
        @Transactional
        void testPostingOn_api_recipes() throws Exception {
                RecipeRequest request = new RecipeRequest();
                request.setTitle("New");
                request.setRecipeDescription("New recipe");
                request.setEstimatedTime(100);
                request.setDescription(new String[] { "First step" });
                request.setName(new String[] { "Flour" });
                request.setQuantity(new int[] { 1 });
                request.setMeasure(new String[] { "" });
                request.setPictures(new MultipartFile[] { new MockMultipartFile("keksi", "keksi.png",
                                MediaType.IMAGE_PNG_VALUE,
                                this.getClass().getResourceAsStream("images/keksi.png").readAllBytes()) });
                mockMvc.perform(MockMvcRequestBuilders.multipart("/api/recipes/").content(new Gson().toJson(request)))
                                .andExpect(status().isOk());
        }

}
