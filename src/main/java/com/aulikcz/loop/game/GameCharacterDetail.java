package com.aulikcz.loop.game;

import java.util.Map;

public record GameCharacterDetail(Long id, String name, String systemCode, Integer templateVersion,
                                  Map<String, Object> sheet, String ownerName, boolean canEdit) {}
