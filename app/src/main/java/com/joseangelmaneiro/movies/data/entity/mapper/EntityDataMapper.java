package com.joseangelmaneiro.movies.data.entity.mapper;

import com.joseangelmaneiro.movies.data.entity.MovieEntity;
import com.joseangelmaneiro.movies.domain.Movie;
import java.util.ArrayList;
import java.util.List;


// Mapper class used to transform MovieEntity, in the data layer, to Movie, in the domain layer.
public class EntityDataMapper {


    EntityDataMapper(){}

    public Movie transform(MovieEntity movieEntity){
        Movie movie = null;
        if(movieEntity != null){
            movie = new Movie(movieEntity.getId(),
                    movieEntity.getVoteAverage(),
                    movieEntity.getTitle(),
                    movieEntity.getPosterPath(),
                    movieEntity.getBackdropPath(),
                    movieEntity.getOverview(),
                    movieEntity.getReleaseDate());
        }
        return movie;
    }

    public List<Movie> transform(List<MovieEntity> movieEntityList){
        List<Movie> movieList = new ArrayList<>();
        for(MovieEntity movieEntity : movieEntityList){
            Movie movie = transform(movieEntity);
            if(movie != null){
                movieList.add(movie);
            }
        }
        return movieList;
    }

}
