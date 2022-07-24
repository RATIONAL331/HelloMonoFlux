package com.example.sec11;

import com.example.sec11.assignment.SlackMember;
import com.example.sec11.assignment.SlackRoom;
import com.example.util.Util;

public class Lec07Assignment {
    public static void main(String[] args) {
        SlackRoom slackRoom = new SlackRoom("room1");

        SlackMember slackMember1 = new SlackMember("member1");
        SlackMember slackMember2 = new SlackMember("member2");
        SlackMember slackMember3 = new SlackMember("member3");

        slackRoom.joinRoom(slackMember1);
        slackRoom.joinRoom(slackMember2);

        slackMember1.says("hello all");
        Util.sleepSeconds(1);
        System.out.println("-----------------------------------------------------");

        slackMember2.says("hi iam2");
        slackMember1.says("hello 2");
        Util.sleepSeconds(1);
        System.out.println("-----------------------------------------------------");

        slackRoom.joinRoom(slackMember3);

        slackMember3.says("hi iam 3");
        slackMember2.says("hello 3 iam 2");
        slackMember1.says("hello 3 iam 1");
        Util.sleepSeconds(1);

        /**
         * member1 --- joined --- room1
         * member2 --- joined --- room1
         * member1 got message: [member1 -> member1]: [hello all]
         * member2 got message: [member1 -> member2]: [hello all]
         * -----------------------------------------------------
         * member1 got message: [member2 -> member1]: [hi iam2]
         * member2 got message: [member2 -> member2]: [hi iam2]
         * member1 got message: [member1 -> member1]: [hello 2]
         * member2 got message: [member1 -> member2]: [hello 2]
         * -----------------------------------------------------
         * member3 --- joined --- room1
         * member3 got message: [member1 -> member3]: [hello all]
         * member3 got message: [member2 -> member3]: [hi iam2]
         * member3 got message: [member1 -> member3]: [hello 2]
         * member1 got message: [member3 -> member1]: [hi iam 3]
         * member2 got message: [member3 -> member2]: [hi iam 3]
         * member3 got message: [member3 -> member3]: [hi iam 3]
         * member1 got message: [member2 -> member1]: [hello 3 iam 2]
         * member2 got message: [member2 -> member2]: [hello 3 iam 2]
         * member3 got message: [member2 -> member3]: [hello 3 iam 2]
         * member1 got message: [member1 -> member1]: [hello 3 iam 1]
         * member2 got message: [member1 -> member2]: [hello 3 iam 1]
         * member3 got message: [member1 -> member3]: [hello 3 iam 1]
         */
    }
}
