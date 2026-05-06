package fse.QueryHandlers;

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

    public String lastQueryItem(String query){
        String[] arr = query.split(" ");
        String type = "";
        String finalItem = "";
        String result;
        boolean flag = true;
        for(int i = arr.length-1; i >=0 && flag; i--){
            if(arr[i].startsWith("path:")){
                type = "path:";
                flag = false;
            }else if(arr[i].startsWith("content:")){
                type = "content:";
                flag = false;
            }
        }
        if(arr[arr.length-1].startsWith("path:")){
            finalItem = arr[arr.length-1].substring(5);
        }else if(arr[arr.length-1].startsWith("content:")){
            finalItem = arr[arr.length-1].substring(8);
        }else{
            finalItem = arr[arr.length-1];
        }
        result = type + finalItem;
        return result;
    }

    public String queryNoLastItem(String query){
        String[] arr = query.split(" ");
        System.out.println(query);
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i <arr.length-1 ; i++){
            stringBuilder.append(arr[i]);
            stringBuilder.append(" ");
        }
        if(arr[arr.length-1].startsWith("path:")){
            stringBuilder.append("path:");
        }else if(arr[arr.length-1].startsWith("content:")){
            stringBuilder.append("content:");
        }
        return stringBuilder.toString();
    }
}
