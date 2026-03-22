
## Simple flowchart
``` mermaid
flowchart TD
    subgraph Group1["Important Section"]
        A
        B
    end
    A["Box\nBox2"] -->|connection name| B["Box2\nBox3"]
    B["Box2\nBox3"] -->|connection name 2| C["Box2\nBox3"]
   
