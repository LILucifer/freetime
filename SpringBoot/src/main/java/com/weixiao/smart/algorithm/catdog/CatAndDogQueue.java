package com.weixiao.smart.algorithm.catdog;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-08-05 21:58.
 */
public class CatAndDogQueue {

    private Queue<PetQueueEntity> catQueue = new LinkedList<>();
    private Queue<PetQueueEntity> dogQueue = new LinkedList<>();

    /**
     * 只有 catQueue dogQueue 没有其他设计的话是比较难做到poAll() 按照入队列的顺序遍历出dog or cat
     * 因此：引入一个计数器来实现两个队列的排序问题
     */
    int count = 0;


    public void add(Pet pet) {
        if (pet instanceof Dog) {
            dogQueue.add(new PetQueueEntity(pet , count++));
        } else if (pet instanceof Cat) {
            catQueue.add(new PetQueueEntity(pet , count++));
        }
    }

    /**
     * 将队列中所有的实例按照进队列的先后顺序依次弹出
     *
     * @return
     */
    public Pet pollAll() {
        if (!isCatEmpty() && !isDogEmpty()) {
            if (catQueue.peek().getCount() < dogQueue.peek().getCount()) {
                return catQueue.poll().getPet();
            }else{
                return dogQueue.poll().getPet();
            }
        }else if (!isDogEmpty()){
            return dogQueue.poll().getPet();
        } else if (!isCatEmpty()) {
            return catQueue.poll().getPet();
        }else{
            throw new RuntimeException("cat an dog queue is empty!");
        }

    }

    public Pet pollCat() {
        if (catQueue.isEmpty()) {
            throw new RuntimeException("cat queue is empty!");
        }
        return catQueue.poll().getPet();
    }

    public Pet pollDog() {
        if (dogQueue.isEmpty()) {
            throw new RuntimeException("dog queue is empty!");
        }
        return dogQueue.poll().getPet();
    }

    public boolean isEmpty() {
        return dogQueue.isEmpty() && catQueue.isEmpty();
    }

    public boolean isDogEmpty() {
        return dogQueue.isEmpty();
    }

    public boolean isCatEmpty() {
        return catQueue.isEmpty();
    }



}
