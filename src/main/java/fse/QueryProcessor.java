package fse;

public class QueryProcessor {

    public QueryData queryProcessor(String query){
        String[] arr = query.split(" ");

        StringBuilder pathStringBuilder = new StringBuilder();
        StringBuilder contentStringBuilder = new StringBuilder();


        int i = 0;
        while(i < arr.length){
            if(arr[i].startsWith("path:")){
                if(!pathStringBuilder.isEmpty()){
                    pathStringBuilder.append(" ");
                }
                pathStringBuilder.append(arr[i].substring(5));
                i++;
            }else if(arr[i].startsWith("content:")){
                if(!contentStringBuilder.isEmpty()){
                    contentStringBuilder.append(" ");
                }
                contentStringBuilder.append(arr[i].substring(8));
                i++;
                while(i<arr.length && (!arr[i].startsWith("path:")) && !arr[i].startsWith("content:")){
                    contentStringBuilder.append(" ");
                    contentStringBuilder.append(arr[i]);
                    i++;
                }
            }else{
                i++;
            }

        }
        String content = contentStringBuilder.toString();
        String path = pathStringBuilder.toString();
        System.out.println("SEARCH CONTENT: " + content);
        System.out.println("SEARCH PATH: " + path);


        QueryData queryData = new QueryData(path,content);


        return queryData;
    }
}
