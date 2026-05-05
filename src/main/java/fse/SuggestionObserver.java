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
        if(queryData.getPath()!=null){
            pathRepo.insertDatabase(queryData.getPath());
        }


    }


}
