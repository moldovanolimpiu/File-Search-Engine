
## System context
``` mermaid
flowchart TD
    subgraph Group1[" "]
        A
    end
    B["User\n[Person]"] -->|Searches for a specific file or files| A["File Search Engine\n[Software System]"]
    A["File Search Engine\n[Software System]"] -->|Gets the information from| C["Computer"]
```

## Container diagram

``` mermaid

flowchart TD
    subgraph Group1[" "]
        A
        B
        C
    end
    C["Frontend Application\n\nThe UI which the client can use to communicate with the application"] --> A["Backend Application\n\nMakes the connection between the UI and the database, performs queries,
implements the search mechanism"]
    A --> B["Database"]

```
   
