fsqrt(N, R) :- Rs is sqrt(N), R is ceiling(Rs).

range(L, L, []).
range(N, L, [N | T]) :- N < L, N1 is N + 1, range(N1, L, T).

cut(A, [H | T] , R) :- M is mod(H, A), M is 0, cut(A, T, R).
cut(A, [H | T] , [H | R]) :- M is mod(H, A), M \= 0, cut(A, T, R).
cut(_, [], []).

init(N) :- N1 is N + 1, fsqrt(N1, Nf), range(2, N1, L), sieve(1, Nf, L).

sieve(A, Last, [H | T]) :- assert(prime(H)), cut(H, T, R), A1 is A + 1, sieve(A1, Last, R).
sieve(A, A, R) :- addAll(R), !.

addAll([H | T]) :- assert(prime(H)), addAll(T).
addAll([]):- !.

nxt_pr(N, R) :- nxt_pr_table(N, R), !.
nxt_pr(N, R) :- ((prime(N), R is N); (N1 is N + 1, nxt_pr(N1, R))), assert(nxt_pr_table(N, R)), !.

composite(N) :- N > 1, \+ prime(N).

first_div(N, D, R) :- D > N, R is N, !.
first_div(N, D, R) :- (prime(N), R is N); 
(0 is mod(N, D), R is D); (0 \= mod(N, D), D1 is D + 1, nxt_pr(D1, Dn) , first_div(N, Dn, R)), !.

divs(N, LastD, [H | T]) :-  N \= 1, first_div(N, LastD, H), ND is div(N, H), divs(ND, H, T), !.  
divs(1, _, []) :- !.

divs(N, R) :- divs-table(N, R), !.
divs(N, R) :- divs(N, 2, R), assert(divs-table(N, R)), !.

mult_(R, L, N) :- mult_table(R, L, N), !.
mult_([H | T], L, N) :- L =< H, mult_(T, H, NN), N is NN * H, assert(mult_table([H | T], L, N)).
mult_([], _, 1).

prime_divisors(N, R) :- var(N), mult_(R, 1, N), N \= 1.
prime_divisors(N, R) :- nonvar(N), N \= 1, divs(N, R).
prime_divisors(1, []) :- !.

nth_prime(1, 2).
nth_prime(2, 3).
nth_prime(3, 5).
nth_prime(N, R) :- nth(3, 5, N, R).
nth(N1, R1, N, R):- N1 < N, R2 is R1 + 1, nxt_pr(R2, Nxt_pr), N2 is N1 + 1, nth(N2, Nxt_pr, N, R).
nth(N, R, N, R).




