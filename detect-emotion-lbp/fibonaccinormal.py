import time

def fib(n):
    if n<2:
        return n
    else:
        return fib(n-1)+fib(n-2)

start_time = time.time()

for index in range(100):
    print(fib(index))


print ("segundos: "+str((time.time()-start_time)))