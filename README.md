# Chess
This is purely my experiment and a first attempt to build computer chess game with Artificial Intelligence (Zhuli).  

# First idea:
First idea is to find best move - move with highest possible weight
* 1 can be reusable for next brain (where each iece is accessed by highest weight to calculate the total rating of the position and than to define what move can improve the rating)
* 1.1 Go through all moves and try to access them by weighting
* 1.2 Sort all moves by weights 
* 2 Sort again by comparing two neighbors and re-accessing their weights comparing to each other
	  
# Second idea 
Second idea is to find the way how to assess position - how to score the position - and to find the move that improves the position to maximum within 2 moves.

* 1 Assess my position
* 1.1 Traverse through each my piece and evaluate its power
* 1.1.1 Move the piece and guess what opponent can do
- Will he/she eat my piece - Can I eat him back = will it be fair exchange and will it give me ability to eat something bigger?
- Will he/she eat my other piece? If yes, will be the same scenario as above?
- Will my move make opponents life harder?
After evaluation all above:		 
- If the piece is active (in strong position) - increase the score (depending on how strong)
- If the piece is passive (needs development) - nothing changes
- If the piece is a victim (loosing the piece or unequal exchange) - decrease the score (depending on how weak)

* 2 Assess my opponents position
* 3 If I am stronger then I need to attack, if I am weaker then I need to defend

Piece power consists of:
 - Piece cost
 - Attacking power (self describing)
 - Defending power (ability to defend other its pieces)
 - Position (how stable position it is)
   
 # TODO
- Logs with debug option on/off in config or main: enter/exit functions, all useful information

# Third idea
## Memory
Introducing Memory that stores:
- All events in life (no event is lost)

## Experience 
One of the main part of Memory is called Experience which is a collection of memory events.
- Each event consist of Content, Appraisal plus one or more links to associations (similar events) that builds a chain of related (associated) events 
- There is no event type as it is hard categorization which needs to be avoided. Instead, events are grouped by associations (similar events) which might have links to other, unrelated events 
- All events in Experience are assessed when they are stored - no modifications allowed after assessment - that simulates mosaic of the experience and forms so called Character - muliti-perosnality. 


## Perception
It is the smallest, simplest and basic entity of Experience. Perceptions is not evaluated and assessed.

## Fragment
a minimum logical grouping of perceptions is called Fragment. Fragment has temporary assessemnt which is cleared when an event is formed.

## Event 
A group of Fragments form Event. All Fragements are assessed before forming Event and Event is scored and classified. After that, all fragments are cleared.
