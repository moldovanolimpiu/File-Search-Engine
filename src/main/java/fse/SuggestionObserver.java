package fse;

import java.sql.SQLException;

public class SuggestionObserver implements Observer {

    PathSuggestionRepo pathRepo = new PathSuggestionRepo();
    QueryProcessor queryProcessorSuggestion = new QueryProcessor();

    public SuggestionObserver() throws SQLException {
    }

    @Override
    public void update(String query) throws SQLException {
        QueryData queryData = queryProcessorSuggestion.queryProcessor(query);
        String[] queryPath = queryData.getPath().split(" ");
        String[] queryContent = queryData.getContent().split(" ");
        if(queryData.getPath()!=null){
            for(String path : queryPath){
                pathRepo.insertDatabase(path);
            }

        }


    }


}
