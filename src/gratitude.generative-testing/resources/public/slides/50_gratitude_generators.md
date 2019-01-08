-----------#Gratitude-application-1

# Generating data for a team-based gratitude application

<style class='before-speaker-note'></style>

* In keeping with the theme of gratitude, let's look at how to generate data for a "team-based gratitude app".
* Imagine it has a registration for new users, a login screen for existing users, and a "Thank someone" page where you can express gratitude.
* Stakeholders intend to generate rewards, like a gift card or post card.  This would be on a monthly basis, for example.

<style></style>

* Team Member UI
    * Registration screen
    * Login screen
    * Message composition screen
* Monthly award generation

-----------#Gratitude-application-user-spec

# User spec definition

```clojure
(ns gratitude.user
  (:require [clojure.string :as string]
            [clojure.spec.alpha :as s]))

(def avatar-url-regex #"(?i)https?://[-a-zA-Z0-9@:%._\\+~#=]+\.[a-z]+[-a-zA-Z0-9@:%_\\+.~#?&//=]*.*")

(s/def ::avatar-url (s/and string? #(re-matches avatar-url-regex %)))
(s/def ::id string?)
(s/def ::full-name string?)

(s/def ::user
  (s/keys :req [::id ::full-name]
          :opt [::avatar-url]))
```

### Try to generate some example users

```clojure
(gen/sample
  (s/gen ::user/user)
  1))                    ; throws because the random string didn't match regex.
                         ; A custom generator is required.
```

--------#Failed-such-that

# Generating a user (for registration)

```clojure
(gen/sample
  (s/gen ::user/user)
  1)

(gen/generate
  (s/gen ::user/avatar-url))
```

Often produces an error:

> `Couldn't satisfy such-that predicate after 100 tries.`

Because usually the randomly-generated avatar-url string won't match the regex.  For this, we need a custom generator.

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Sane_users

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Insane_users

--------#Gratitude-application-user-generation-url

# User generation

```clojure
(ns gratitude.data-generators           ; Generators can be put in non-production "test" namespaces.
  (:require [gratitude.user :as user])  ; References are omitted to fit on slide

(s/def ::sane-avatar-url
  (-> #(re-matches user/avatar-url-regex %)
      (s/with-gen #(gen/fmap string/join
                      (gen/tuple
                        (s/gen #{"http://" "https://"})
                        (gen/such-that seq (gen/string-alphanumeric))
                        (s/gen #{".com" ".us" ".co" ".org"})
                        (gen/return "/")
                        (gen/string-alphanumeric))))))

(s/def ::insane-avatar-url
  (-> #(re-matches user/avatar-url-regex %)
      (s/with-gen #(gen/fmap string/join
                      (gen/tuple
                        (s/gen #{"http://" "https://"})
                        (gen/string-alphanumeric)
                        (s/gen #{".com" ".us" ".co" ".org"})
                        (s/gen string?))))))
```

--------#Gratitude-application-user-generation-email

# User generation (User id/Email)

```clojure
(s/def ::sane-email-address
 (s/with-gen string? #(gen/fmap string/join
                        (gen/tuple
                            (gen/such-that seq (gen/string-alphanumeric))
                            (gen/return "@")
                            (gen/such-that seq (gen/string-alphanumeric))
                            (s/gen #{".com" ".us" ".co" ".org"}))))))
(s/def ::insane-email-address
 (s/with-gen string? #(gen/fmap string/join
                        (gen/tuple
                            (s/gen string?)
                            (s/gen #{"@" " "})
                            (s/gen string?)
                            (gen/return ".")
                            (s/gen string?))))))
(s/def ::user-id
 (s/with-gen string? #(gen/frequency
                        [[9 (s/gen ::sane-email-address)]
                         [1 (s/gen ::insane-email-address)]]))))
```

--------#Gratitude-application-user-generation-person-name

# User generation (person name)

```clojure
(defn insane-string-generator
  [gen-fn]
  #(gen/frequency
      [[9 (gen-fn)]]
      [[1 (gen/string-alphanumeric)]]
      [[1 (s/gen string?)]]))

(def sane-first-name
  #(s/gen (into #{} (string/split "Su,Jo,Bob,Al,Bil,Jon," ","))))

(def insane-first-name
  (insane-string-generator sane-first-name))

(def sane-last-name
  #(s/gen (into #{} (string/split "Smith,Rogers,Jones,X,Ling,Shih" ","))))

(def insane-last-name
  (insane-string-generator sane-last-name))
```

-------#Gratitude-application-user-generation-person-full-name

# User generation (full name)

```clojure
(def sane-full-name
  #(gen/frequency
      [[6 (gen/fmap (partial string/join " ") ;   "first last"
              (gen/tuple
                (sane-first-name)
                (sane-last-name)))]
       [2 (gen/fmap (partial string/join ", ")  ; "last, first"
              (gen/tuple
                (sane-last-name)
                (sane-first-name)))]
       [2 (gen/fmap (partial string/join " ")   ; "name name name"
                (gen/list (sane-first-name)))]]))

(def insane-full-name
  #(gen/frequency
      [[6 (gen/fmap (partial string/join " ") ;   "first last"
              (gen/tuple
                (insane-first-name)
                (insane-last-name)))]
       [2 (gen/fmap (partial string/join ", ")  ; "last, first"
              (gen/tuple
                (insane-last-name)
                (insane-first-name)))]
       [2 (gen/fmap (partial string/join " ")   ; "name name name"
                (gen/list (insane-first-name)))]]))
```

--------#Gratitude-application-user-generation-custom-generators-1

# User generation (mix-in)

```clojure
(defn with-sane-user-generators
  [generators]
  (merge generators
    {::user/id    #(s/gen ::sane-email-address generators)
     ::user/avatar-url #(s/gen ::sane-avatar-url generators)
     ::user/full-name  sane-full-name}))

(defn with-insane-user-generators
  [generators]
  (merge generators
    {::user/id    #(s/gen ::insane-email-address generators)
     ::user/avatar-url #(s/gen ::insane-avatar-url generators)
     ::user/full-name  insane-full-name}))
```

--------#Gratitude-application-user-generation-custom-generators-2

# User generation (mix-in)

```clojure
(defn with-static-user-generators
  [generators]
  (merge generators
    {::user/id    #(gen/return "jesse.doe@mail.example.com")
     ::user/avatar-url #(gen/return "https://picsum.photos/20/20/?random")
     ::user/full-name  #(gen/return "Jesse Doe")}))

(defn with-codemash-user-generators
  [generators]
  (merge generators
    {::user/id
      #(gen/fmap string/join
          (gen/tuple
            (gen/return "Hello.CodeMash")
            (gen/return "@")
            (gen/string-alphanumeric)
            (s/gen #{".com" ".us" ".co" ".org"})))}))
```

--------#Thank-you-note-spec

# Thank you notes definition

```clojure
(ns gratitude.expression  (comment "references omitted to fit on slide"))

(s/def ::tag string?)

(s/def ::message string?)
(s/def ::cheers pos-int?)
(s/def ::from ::user/id)
(s/def ::to (s/coll-of ::user/id :min-count 1))
(s/def ::tags (s/coll-of ::tag :into #{}))
(s/def ::time-of-record inst?)

(s/def ::thank-you-note
  (s/keys :req [::from ::to ::message ::cheers ::tags ::time-of-record]))
```

--------#Thank-you-note-samples

# Thank you notes samples

```clojure
  (gen/sample
    (s/gen ::user/user
      (-> {}
          gratitude.data-generators/with-insane-user-generators))
    10) ; insane users

  (gen/sample
    (s/gen ::user/user
      (-> {}
          gratitude.data-generators/with-sane-user-generators))
    5) ; sane users
```

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Sane_users_thank-you-notes

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Insane_users_thank-you-notes

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/CodeMash_users_thank-you-notes

--------#Awards-event-specs

# Awards system testing (sequence of events)

```clojure
(ns gratitude.award-generators (comment "references omitted to fit on slide")))

(s/def ::interval pos-int?)

;; specs for events
(s/def ::delay (s/keys :req [::interval]))
(s/def ::new-user (s/keys :req [::user/user]))
(s/def ::new-note ::expression/thank-you-note)
(s/def ::generate-report #{:generate-report})

(s/def ::event (s/or ::delay ::delay,
                     ::new-user ::new-user
                     ::new-note ::new-note
                     ::generate-report ::generate-report))
(s/def ::events (s/coll-of ::event))

;(gen/sample (s/gen ::events))
```

--------#Awards-data

# Awards data

The awards are generated by interpretting the thank you messages and user registrations.

* "New User!" awards for signing up
* Awards for getting the most cheers
* Awards for receiving gratitude with the coveted "Grooviness" tag

--------#Randomness-ahead

# Warning: Randomness ahead

These next slides take a moment to generate new data and load...

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Awards-data-one-scenario

--------
#!/gratitude.generative_testing.section_50_gratitude_generators/Visualized-awards-data-one-scenario
