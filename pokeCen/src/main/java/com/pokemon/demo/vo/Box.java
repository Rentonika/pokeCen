package com.pokemon.demo.vo;

import lombok.Data;

@Data
public class Box {
	private int boxno; // 박스 관리 넘버
	private String trainer_id; // 해당 박스를 소유한 트레이너 넘버
	private int trainer_box_no; // 트레이너의 몇 번째 박스인지
	private int amount; // 몇 마리까지 차 있는지 -> 삭제해도 되지 않을까?

}
