%[(K, V), [(LK, LV), ..], [(RK, RV), ..]]

right_rotate([P, [X, XL, XR], PR], [X, XL, [P, XR, PR]]) :- !.
left_rotate([P, PL, [X, XL, XR]], [X, [P, PL, XL], XR]) :- !.

splay([(K, V), L, R], K, [(K, V), L, R]) :- !.

splay([P, [(K, V), XL, XR], PR], K, ANS) :- right_rotate([P, [(K, V), XL, XR], PR], ANS), !.
splay([P, PL, [(K, V), XL, XR]], K, ANS) :- left_rotate([P, PL, [(K, V), XL, XR]], ANS), !.

splay([G, [P, [(K, V), XL, XR], PR], GR], K, ANS) :- right_rotate([G, [P, [(K, V), XL, XR], PR], GR], T), right_rotate(T, ANS), !.
splay([G, GL, [P, PL, [(K, V), XL, XR]]], K, ANS) :- left_rotate([G, GL, [P, PL, [(K, V), XL, XR]]], T), left_rotate(T, ANS), !.

splay([G, [P, RL, [(K, V), XL, XR]], GR], K, ANS) :- left_rotate([P, RL, [(K, V), XL, XR]], T), right_rotate([G, T, GR], ANS), !.
splay([G, GL, [P, [(K, V), XL, XR], PR]], K, ANS) :- right_rotate([P, [(K, V), XL, XR], PR], T), left_rotate([G, GL, T], ANS), !.

splay([(XK, XV), L, R], K, ANS) :- XK > K, splay(L, K, [(K, V), TL, TR]), ANS = [(XK, XV), [(K, V), TL, TR], R], !.
splay([(XK, XV), L, R], K, ANS) :- XK > K, splay(L, K, T), splay([(XK, XV), T, R], K, ANS), !.

splay([(XK, XV), L, R], K, ANS) :- XK < K, splay(R, K, [(K, V), TL, TR]), ANS = [(XK, XV), L, [(K, V), TL, TR]], !.
splay([(XK, XV), L, R], K, ANS) :- XK < K, splay(R, K, T), splay([(XK, XV), L, T], K, ANS), !.

tree_put([], (K, V), [(K, V), [], []]) :- !.
tree_put([X, [], R], (K, V), [X, [(K, V), [], []], R]) :- !.
tree_put([X, L, R], (K, V), [X, ANS, R]) :- tree_put(L, (K, V), ANS), !.

balance([], T, T) :- !.
balance([(K, _)], T, ANS) :- splay(T, K, ANS), !.
balance([(K, _) | R], T, ANS) :- splay(T, K, NT), balance(R, NT, ANS), !.

make_tree([], []) :- !.
make_tree([(K, V)], [(K, V), [], []]) :- !.
make_tree([(K, V) | T], ANS) :- make_tree(T, NT), tree_put(NT, (K, V), X), splay(X, K, SX), splay(SX, K, ANS), !.

tree_build(L, M) :- make_tree(L, T), balance(L, T, M).

map_get([(K, V), _, _], K, V) :-  !.
map_get([(XK, _), L, _], K, ANS) :- K < XK, map_get(L, K, ANS), !.
map_get([(XK, _), _, R], K, ANS) :- K > XK, map_get(R, K, ANS), !.

map_size([], 0) :- !.
map_size([(_, _), L, R], ANS) :- map_size(L, X), map_size(R, Y), PRE_ANS is X + Y, ANS is PRE_ANS + 1.
