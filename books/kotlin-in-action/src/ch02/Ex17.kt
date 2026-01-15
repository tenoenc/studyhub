package ch02.ex17

interface Expr
class Num(val value: Int): Expr
class Sum(val left: Expr, val right: Expr): Expr