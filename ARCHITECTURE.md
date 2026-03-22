
## Simple flowchart
``` mermaid
flowchart TD
    subgraph Group1[""]
        A
    end
    B["Box\nBox2"] -->|connection name| A["Box2\nBox3"]
    A["Box2\nBox3"] -->|connection name 2| C["Box2\nBox3"]
   
