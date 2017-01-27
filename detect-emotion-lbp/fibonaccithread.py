import time
from multiprocessing import Pool

def fib(n):
    if n<2:
        return n
    else:
        return fib(n-1)+fib(n-2)

def print_fib(n):
    print(fib(n))

if __name__ == "__main__":
    p=Pool(3)

    start_time = time.time()

    results = p.map(print_fib,range(100))

    print ("segundos: "+str((time.time()-start_time)))