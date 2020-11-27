
# ItemExtension  
This minestom extension handles the items for Cepi.  

It uses a trait based system to handle items.  
   
## Traits  
  Traits represent a subset of data on an item. There are Trait Containers and Traits.  
   
 In this case, an Item is a TraitContainer, as it can render and contain traits.  
   
 Traits have 2 functions -- rendering lore and rendering tasks.  
   
### Lore function  
The lore is for display priority, sorting it based on the index where it needs  
 to go.  
   
 For example, if Trait A has a higher lore priority, it appears higher on the lore list:  
   
```  
Trait A (priority = 2)
Trait B (priotity = 1)
```

### Task function

This is for the functionality of the trait, which also has its own seperate priority, in order to allow dependencies. An example of this found in the Item system is the Name trait, which optionally depends on the Rarity trait to set its color. It's task priority runs top first, then goes down to the bottom.