
## System context
``` mermaid
flowchart TD
    subgraph Group1[" "]
        A
    end
    B["User\n[Person]\nThe user who searches for any file"] -->|Searches for a specific file or files| A["File Search Engine\n[Software System]"]
    A["File Search Engine\n[Software System]"] -->|Interacts with the computer to get the files| C["Computer"]
```

## Container diagram

``` mermaid

flowchart TD
    subgraph Group1[" "]
        A
        B
        C
    end
    D["User\n[Person]\nThe user who searches for any file"] -->|Searches for a specific file or files| C
    C["Frontend Application\n\nThe UI which the client can use to communicate with the application"] --> A["Backend Application\n\nMakes the connection between the UI and the database, performs queries,
implements the search mechanism"]
    A --> B["Database\n[Relational database schema]\nIndexes and stores file data"]
    A -->|Interacts with the computer to get the files| E["Computer"]

```
   
