// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package org.example.sca.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
}
