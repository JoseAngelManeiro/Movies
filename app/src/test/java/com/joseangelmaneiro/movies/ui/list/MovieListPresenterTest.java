package com.joseangelmaneiro.movies.ui.list;

import com.joseangelmaneiro.movies.data.Handler;
import com.joseangelmaneiro.movies.data.Movie;
import com.joseangelmaneiro.movies.data.MoviesRepository;
import com.joseangelmaneiro.movies.ui.Formatter;
import com.joseangelmaneiro.movies.utils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MovieListPresenterTest {


    private static final String URL_TO_DISPLAY = "fake_url";

    private MovieListPresenter sut;
    @Mock
    private MoviesRepository repository;
    @Mock
    private Formatter formatter;
    @Mock
    private MovieListView view;
    @Mock
    private MovieCellView cellView;
    @Captor
    private ArgumentCaptor<Handler<List<Movie>>> moviesHandlerCaptor;
    @Captor
    private ArgumentCaptor<String> textCaptor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sut = new MovieListPresenter(repository, formatter);
        sut.setView(view);

        when(formatter.getCompleteUrlImage(anyString())).thenReturn(URL_TO_DISPLAY);
    }

    @After
    public void tearDown() throws Exception {
        sut = null;
    }

    @Test
    public void viewReady_InvokesGetMovies(){
        sut.viewReady();

        verify(repository).getMovies(any(Handler.class));
    }

    @Test
    public void refresh_InvokesGetMovies(){
        sut.refresh();

        verify(repository).getMovies(any(Handler.class));
    }

    @Test
    public void invokeGetMovies_SavesMovies(){
        // The list is empty when starting
        assertTrue(sut.moviesListIsEmpty());

        sut.invokeGetMovies();

        setMoviesAvailable(TestUtils.createMainMovieList());

        // If repository returns movies, they are saved
        assertFalse(sut.moviesListIsEmpty());
    }

    @Test
    public void invokeGetMovies_RefreshesView(){
        sut.invokeGetMovies();

        setMoviesAvailable(TestUtils.createMainMovieList());

        verify(view).cancelLoadingDialog();
        verify(view).refresh();
    }

    @Test
    public void invokeGetMovies_ShowsError(){
        sut.invokeGetMovies();

        setMoviesError();

        verify(view).cancelLoadingDialog();
        verify(view).showErrorMessage();
    }

    @Test
    public void getItemsCount_ReturnsZeroWhenThereIsNoData(){
        assertEquals(0, sut.getItemsCount());
    }

    @Test
    public void getItemsCount_ReturnsNumberOfItems(){
        int itemsExpected = 10;
        sut.saveMovies(TestUtils.createMovieList(itemsExpected));

        assertEquals(itemsExpected, sut.getItemsCount());
    }

    @Test
    public void configureCell_DisplaysImage(){
        sut.saveMovies(TestUtils.createMainMovieList());

        sut.configureCell(cellView, 1);

        verify(cellView).displayImage(textCaptor.capture());
        assertEquals(URL_TO_DISPLAY, textCaptor.getValue());
    }

    @Test
    public void onItemClick_SavesSelectedMovieId(){
        List<Movie> movieList = TestUtils.createMainMovieList();
        int itemSelected = 0;
        int idExpected = movieList.get(itemSelected).getId();
        sut.saveMovies(movieList);

        sut.onItemClick(idExpected);

        assertEquals(idExpected, sut.getSelectedMovieId());
    }


    private void setMoviesAvailable(List<Movie> movieList) {
        verify(repository).getMovies(moviesHandlerCaptor.capture());
        moviesHandlerCaptor.getValue().handle(movieList);
    }

    private void setMoviesError() {
        verify(repository).getMovies(moviesHandlerCaptor.capture());
        moviesHandlerCaptor.getValue().error();
    }

}