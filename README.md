IndexingExercise
=================

##Entry Points:
* Run IndexConstructor.java \<location_to_save_index>
* Run QueryServer.java \<location_to_read_index>

##Assumptions:
* Matching is first character inclusive, but middle of string underscore exclusive. For example, __abc__def_ghi would match strings like __abc..,_abc..,abc..,_def..,ghi. Not __def,_ghi.
* Name length is limited to a max of 65,000 characters, so (max search query) << n.
* Preconditions on name and score are met, so they are not checked.
* No deletion from index required.

## Design Overview:
A container object was created to hold the name and score pair, called datum. The primary index structure consists of an array of tries, with each trie root (element of the array) corresponding to the first character of a string. The biggest challenge was prefix matching queries with underscores. This was solved by separating the leading underscores from the strings. First, a list of all possible full prefix substrings were generated (for example: abc__def = abc, _def) and added individually to the tries. Before adding, underscores would be counted and stripped from the substrings (ex: _def = def w/ 1 underscore). During insertion, the string and score would be added to each node of the trie. Each node has an unbounded array called underscores, and each element of this array would hold a top 10 ranking of names that pass through that node (ex: _def would be added to underscores[1] since it has 1 underscore, but it would also be added to underscores[0] since 'def' would match the search). To prevent duplicate insertion, the node is updated with the current string number (coinciding with the new size of the index), and the maximum number of underscores previously traversed. If the node is traversed again for the same string, the string number will match. The score will only be updated for the same string if the underscore number is greater than the one previously recorded for the string (and only to the ones which have not already had the element added).

## Development Process:
An iterative test driven development process was used. Basic structure of index was determined, and it was made to work with just letters, then leading underscores, then underscores in the middle of a string. (see IndexTester.java)

##Complexity:
At the expense of memory, search complexity should be proportional to the length of the search query, k, or O(k), where the underlying assumption is that k << n.