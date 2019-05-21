find([(K, V) | _], K, V) :- !.
find([_ | T], K, X) :- find(T, K, X).

map_get(L, K, V) :- find(L, K, V).

insert([], K, V, [(K, V)]) :- !.
insert([(A, B) | T], K, V, [(K, V) | X]) :- A >= K, insert(T, A, B, X), !.
insert([(A, B) | T], K, V, [(A, B) | X]) :- A =< K, insert(T, K, V, X).

replace([(K, _) | T], K, V, [(K, V) | T]) :- !.
replace([H | T], K, V, [H | X]) :- replace(T, K, V, X).

map_put(L, K, V, X) :-  find(L, K, _), replace(L, K, V, X), !.
map_put(L, K, V, X) :- \+ find(L, K, _), insert(L, K, V, X).

erase([(K, _) | T], K, T) :- !.
erase([H | T], K, [H | X]) :- erase(T, K, X).

map_remove(L, K, X) :- find(L, K, _), erase(L, K, X).
map_remove(L, K, X) :- \+ find(L, K, _), X = L.
