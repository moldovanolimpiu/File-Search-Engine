package fse.SuggestionObserverPattern;

import fse.QueryHandlers.QueryData;
import fse.QueryHandlers.QueryProcessor;
import fse.Repositories.ContentSuggestionRepo;
import fse.Repositories.PathSuggestionRepo;

import java.sql.SQLException;

public class SuggestionObserver implements Observer {

    QueryProcessor queryProcessorSuggestion = new QueryProcessor();

    public SuggestionObserver() throws SQLException {
    }

    @Override
    public void update(String query, PathSuggestionRepo pathRepo, ContentSuggestionRepo contentRepo) throws SQLException {
        QueryData queryData = queryProcessorSuggestion.queryProcessor(query);
        String[] queryPath = queryData.getPath().split(" ");
        String[] queryContent = queryData.getContent().split(" ");
        if(queryData.getPath()!=null){
            for(String path : queryPath){
                pathRepo.insertDatabase(path);
            }

        }
        if(queryData.getContent()!=null){
            for(String content : queryContent){
                contentRepo.insertDatabase(content);
            }
        }


    }


}
